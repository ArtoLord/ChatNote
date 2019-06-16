package com.yshmgrt.chat.view

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
import com.yshmgrt.chat.MainActivity
import com.yshmgrt.chat.R
import com.yshmgrt.chat.adapters.MessageViewAdapter
import com.yshmgrt.chat.adapters.TagViewAdapter
import com.yshmgrt.chat.data_base.Controller
import com.yshmgrt.chat.data_base.dataclasses.Attachment
import com.yshmgrt.chat.data_base.dataclasses.SQL_Message
import com.yshmgrt.chat.data_base.dataclasses.Tag
import com.yshmgrt.chat.message.logic.Logic
import kotlinx.android.synthetic.main.main_chat_fragment.*
import kotlinx.android.synthetic.main.main_chat_fragment.view.*
import kotlinx.android.synthetic.main.search_fragment.*
import kotlinx.android.synthetic.main.search_fragment.view.*
import org.jetbrains.anko.bundleOf
import java.util.*

class MainChatFragment : Fragment() {

    var adapter: MessageViewAdapter? = null
    private lateinit var linearLayoutManager: LinearLayoutManager
    val messageList = mutableListOf<Long>()
    fun updateMessageList(controller: Controller,tag:Long = -1, onUpdate:(List<Long>)->Unit){
        if (tag == -1L){
                controller.getAllMessageId{ exit->
                    messageList.clear()
                    messageList.addAll(exit)
                    onUpdate(exit)
            }
        }
        else{
            controller.getMessagesByTagId(tag){exit->
                messageList.clear()
                messageList.addAll(exit)
                onUpdate(exit)
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
            updateMessageList(controller,_id){
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


        view.send_button.setOnClickListener {
            if (view.message_edit_text.text.isNotEmpty()){
                val log = Logic(view.message_edit_text.text.toString()).getTags()
                val tags = List(log.size){Tag(123,log[it])}
                val attachments = listOf<Attachment>()
                controller.sendMessage(SQL_Message(123,view.message_edit_text.text.toString(),Date().time),tags,attachments){
                    updateMessageList(controller){
                        adapter!!.notifyDataSetChanged()
                        view.message_edit_text.text.clear()
                        view.message_list_1.smoothScrollToPosition(adapter!!.itemCount - 1)
                    }
                }

            }
        }

        view.attach_button.setOnClickListener {
            (activity as MainActivity).openDrawer()
        }

        view.search_view_back.setOnClickListener {
            changeSearchState()
        }

        updateMessageList(controller){
            adapter!!.notifyDataSetChanged()
            view.message_list_1.smoothScrollToPosition(adapter!!.itemCount - 1)
        }

        return view
    }

    fun onSearchVisible(){
        val controller = Controller(context!!)
        val tagList = mutableListOf<Long>()
        val tagAdapter = TagViewAdapter(tagList){
            val _id = it.tag.toString().toLong()
            updateMessageList(controller,_id){
                adapter!!.notifyDataSetChanged()
                message_list_1.smoothScrollToPosition(adapter!!.itemCount - 1)
            }
        }
        /*
        val linearTagLayoutManager = LinearLayoutManager(context!!.applicationContext)
        linearTagLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
        tagsRecycle.layoutManager = linearTagLayoutManager
        tagsRecycle.adapter = tagAdapter
        */
        editSearch.afterTextChanged {
            controller.getMessageByMessagePatr(it){exit->
                val a = mutableSetOf<Long>()
                a.addAll(exit)

                messageList.clear()
                messageList.addAll(a)
                adapter!!.notifyDataSetChanged()
                message_list_1.smoothScrollToPosition(adapter!!.itemCount - 1)
                Log.d("Work",exit.size.toString())

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
        else{
            updateMessageList(Controller(context!!)){
                adapter!!.notifyDataSetChanged()
                message_list_1.smoothScrollToPosition(adapter!!.itemCount - 1)
            }
        }
    }
}