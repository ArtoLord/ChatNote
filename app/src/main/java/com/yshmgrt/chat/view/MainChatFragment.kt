package com.yshmgrt.chat.view

import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.beust.klaxon.Klaxon
import com.yshmgrt.chat.MainActivity
import com.yshmgrt.chat.R
import com.yshmgrt.chat.adapters.MessageRowAdapter
import com.yshmgrt.chat.adapters.MessageRowViewHolder
import com.yshmgrt.chat.adapters.MessageViewAdapter
import com.yshmgrt.chat.adapters.TagViewAdapter
import com.yshmgrt.chat.data_base.Controller
import com.yshmgrt.chat.data_base.dataclasses.Attachment
import com.yshmgrt.chat.data_base.dataclasses.SQL_Message
import com.yshmgrt.chat.data_base.dataclasses.Tag
import com.yshmgrt.chat.message.TagView
import com.yshmgrt.chat.message.attachments.IAttachment
import com.yshmgrt.chat.message.attachments.Image
import com.yshmgrt.chat.message.attachments.ImageAttachment
import com.yshmgrt.chat.message.logic.Logic
import kotlinx.android.synthetic.main.bottom_drawer_fragment.*
import kotlinx.android.synthetic.main.bottom_drawer_fragment.view.*
import kotlinx.android.synthetic.main.main_chat_fragment.*
import kotlinx.android.synthetic.main.main_chat_fragment.view.*
import kotlinx.android.synthetic.main.search_fragment.*
import kotlinx.android.synthetic.main.search_fragment.view.*
import kotlinx.android.synthetic.main.tag_view.view.*
import org.jetbrains.anko.bundleOf
import java.util.*

class MainChatFragment : Fragment() {

    var tagList = mutableSetOf<Long>()

    var adapter: MessageViewAdapter? = null
    private lateinit var linearLayoutManager: LinearLayoutManager
    val messageList = mutableListOf<Long>()
    fun updateMessageList(controller: Controller,tag:Collection<Long>  = emptyList(), onUpdate:(Collection<Long>)->Unit){
        if (tag.isEmpty()){
                controller.getAllMessageId{ exit->
                    messageList.clear()
                    messageList.addAll(exit)
                    onUpdate(exit)
            }
        }
        else{
            controller.getMessagesByTags(tag.toList()){exit->
                Log.d("TAGS_DEBUG", exit.size.toString())
                messageList.clear()
                messageList.addAll(exit)
                onUpdate(exit)
            }
        }
    }

    fun updateTagProvider(view:View, controller: Controller){
        view.tag_provider.removeAllViews()
        for (i in tagList){
            controller.getTagById(i){
                val tv = TagView(context!!)
                tv.tag = it._id
                tv.tag_text.text = it.text
                tv.close_button.visibility = View.VISIBLE
                tv.setOnClickListener {_->
                    val mlist = tagList.toMutableList()
                    mlist.remove(it._id)
                    tagList = mlist.toMutableSet()
                    updateTagProvider(view,controller)
                    updateMessageList(controller,mlist){
                        adapter!!.notifyDataSetChanged()
                    }
                }
                view.tag_provider.addView(tv)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    lateinit var tagsCard:View
    lateinit var tagsRecycle:RecyclerView
    lateinit var editSearch:EditText

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.main_chat_fragment, container, false)

        val controller = Controller(context!!)

        tagsRecycle = view.tags_search_recycler
        tagsCard = view.search_view
        editSearch = view.search_edit_text
        adapter = MessageViewAdapter(messageList,{
            val _id = it.tag.toString().toLong()
            tagList.add(_id)
            updateTagProvider(view,controller)
            updateMessageList(controller, tagList){
                adapter!!.notifyDataSetChanged()
                view.message_edit_text.text.clear()
                view.message_list_1.smoothScrollToPosition(adapter!!.itemCount - 1)
            }
        }){
            val _id = it.tag.toString().toLong()
            val bundle = bundleOf("messageId" to _id)

            Navigation.findNavController(view).navigate(R.id.action_mainChatFragment_to_messageFragment,bundle)
        }
        linearLayoutManager = LinearLayoutManager(context!!.applicationContext)
        linearLayoutManager.stackFromEnd = true
        view.message_list_1.layoutManager = linearLayoutManager
        view.message_list_1.adapter = adapter

        val attachmentList = mutableListOf<Attachment>()
        view.send_button.setOnClickListener {
            if (view.message_edit_text.text.isNotEmpty() || attachmentList.isNotEmpty()){
                val log = Logic(view.message_edit_text.text.toString()).getTags()
                val tags = List(log.size){Tag(123,log[it])}
                controller.sendMessage(SQL_Message(123,view.message_edit_text.text.toString(),Date().time,SQL_Message.USER_TYPE),tags,attachmentList){
                    updateMessageList(controller){
                        adapter!!.notifyDataSetChanged()
                        view.message_edit_text.text.clear()
                        view.message_list_1.smoothScrollToPosition(adapter!!.itemCount - 1)
                        attachmentList.clear()
                        view.attachments_view.removeAllViews()
                    }
                }

            }
        }

        view.attach_button.setOnClickListener {
            (activity as MainActivity).openDrawer{
                it.add_image.setOnClickListener{
                    ImageAttachment.sendIntentToPick(activity!!)
                }
            }
        }


        view.search_view_back.setOnClickListener {
            changeSearchState()
        }
        updateMessageList(controller){
            adapter!!.notifyDataSetChanged()
            view.message_list_1.smoothScrollToPosition(adapter!!.itemCount - 1)
        }



        (activity as MainActivity).onFragmentResult = {requestCode, resultCode, data->
            if (requestCode==MainActivity.PIC_IMAGE_REQUEST){
                val uri = data!!.data
                val attach = Attachment(123,Attachment.IMAGE_TYPE.toString(),
                    Klaxon().toJsonString(Image(MainActivity.getRealPathFromUri(context!!,uri as Uri))),123)
                attachmentList.add(attach)
                view.attachments_view.addView(IAttachment.create(attach)!!.getPreview(context!!))
            }
        }


        return view
    }

    fun onSearchVisible(){
        val controller = Controller(context!!)
        val messageList = mutableListOf<Long>()
        val tagList = mutableListOf<Long>()

        val tagAdapter = MessageRowAdapter(messageList){
            val _id = it.tag.toString().toLong()
            val bundle = bundleOf("messageId" to _id)
            Navigation.findNavController(it).navigate(R.id.action_mainChatFragment_to_messageFragment,bundle)
            changeSearchState()
        }

        val linearTagLayoutManager = LinearLayoutManager(context!!.applicationContext)
        search_message_recycler.layoutManager = linearTagLayoutManager
        search_message_recycler.adapter = tagAdapter
        controller.getMessageByMessagePatr(search_edit_text.text.toString()){exit->
            val a = mutableSetOf<Long>()
            a.addAll(exit)

            messageList.clear()
            messageList.addAll(a)
            tagAdapter.notifyDataSetChanged()
        }
        if (search_edit_text.text.toString().isEmpty()){
            messages_card.visibility = View.GONE
        }
        else{
            messages_card.visibility = View.VISIBLE
        }
        search_edit_text.afterTextChanged {
            messages_card.visibility = View.VISIBLE
            controller.getMessageByMessagePatr(it){exit->
                val a = mutableSetOf<Long>()
                a.addAll(exit)

                messageList.clear()
                messageList.addAll(a)
                tagAdapter.notifyDataSetChanged()
            }
            /*
            controller.getTagByTagPart(it){exit->
                val a = mutableSetOf<Long>()
                a.addAll(exit)
                tagList.clear()
                tagList.addAll(a)
                tagAdapter.notifyDataSetChanged()
                */
            }
        }



    fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
        this.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(editable: Editable?) {
                afterTextChanged.invoke(editable.toString())
            }
        })
    }

    var searchVisible = false

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.search_button){
            changeSearchState()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun changeSearchState() {
        val status = when(searchVisible){
            true-> View.GONE
            false->View.VISIBLE
        }
        tagsCard.visibility = status

        searchVisible = !searchVisible
        if(searchVisible){
            onSearchVisible()
        }
    }

    fun test(){
        Log.d("DEBUG","It Works")
    }
}