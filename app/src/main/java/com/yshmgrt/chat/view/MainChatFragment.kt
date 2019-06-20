package com.yshmgrt.chat.view

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.NavigatorProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.beust.klaxon.Klaxon
import com.yshmgrt.chat.MainActivity
import com.yshmgrt.chat.R
import com.yshmgrt.chat.adapters.MessageRowAdapter
import com.yshmgrt.chat.adapters.MessageViewAdapter
import com.yshmgrt.chat.data_base.Controller
import com.yshmgrt.chat.data_base.dataclasses.Attachment
import com.yshmgrt.chat.data_base.dataclasses.Link
import com.yshmgrt.chat.data_base.dataclasses.SQL_Message
import com.yshmgrt.chat.data_base.dataclasses.Tag
import com.yshmgrt.chat.message.TagView
import com.yshmgrt.chat.message.attachments.AttachmentView
import com.yshmgrt.chat.message.attachments.images.Image
import com.yshmgrt.chat.message.attachments.images.ImageAttachment
import com.yshmgrt.chat.message.attachments.notification.Notification
import com.yshmgrt.chat.message.attachments.notification.NotificationAttachment
import com.yshmgrt.chat.message.logic.Logic
import kotlinx.android.synthetic.main.attachment_background_card.view.*
import kotlinx.android.synthetic.main.bottom_drawer_fragment.view.*
import kotlinx.android.synthetic.main.current_message_view.view.*
import kotlinx.android.synthetic.main.main_chat_fragment.view.*
import kotlinx.android.synthetic.main.search_fragment.*
import kotlinx.android.synthetic.main.search_fragment.view.*
import kotlinx.android.synthetic.main.tag_view.view.*
import kotlinx.android.synthetic.main.tag_view.view.close_button
import org.jetbrains.anko.bundleOf
import java.text.SimpleDateFormat
import java.util.*

class MainChatFragment : Fragment() {

    var tagList = mutableSetOf<Long>()
    var parentID = -1L

    var sendStats = true
    var editableMessageId = -1L
    val parentStack = Stack<Long>()

    var adapter: MessageViewAdapter? = null
    private lateinit var linearLayoutManager: LinearLayoutManager
    val messageList = mutableListOf<Long>()

    var state = State.NONE


    private fun updateBackButton() {
        Log.d("State1", state.toString())
        val toolbar = (activity as AppCompatActivity).supportActionBar!!
        if (state <= 1) {
            toolbar.setDisplayHomeAsUpEnabled(false)
            toolbar.setDisplayUseLogoEnabled(true)
        }
        else {
            toolbar.setDisplayHomeAsUpEnabled(true)
            toolbar.setDisplayUseLogoEnabled(false)
        }
    }

    fun updateMessageList(
        controller: Controller,
        tag: Collection<Long> = tagList,
        onUpdate: (Collection<Long>) -> Unit
    ) {
        val temp = tag.toMutableList()
        temp.add(parentID)
        Log.d("TAGS_DEBUG", parentID.toString())
        controller.getMessagesByTags(temp) { exit ->
            messageList.clear()
            messageList.addAll(exit)
            onUpdate(exit)
        }
    }

    fun updateTagProvider(view: View, controller: Controller) {
        view.tag_provider.removeAllViews()
        val temp = tagList
        state = if (tagList.size > 0)
            state or State.TAG
        else
            state xor State.TAG
        updateBackButton()
        for (i in temp) {
            controller.getTagById(i) {
                if (it.type == Tag.USER_TYPE) {
                    val tv = TagView(context!!)
                    tv.tag = it._id
                    tv.tag_text.text = it.text
                    tv.close_button.visibility = View.VISIBLE
                    tv.setOnClickListener { _ ->
                        val mlist = tagList.toMutableList()
                        mlist.remove(it._id)
                        tagList = mlist.toMutableSet()
                        updateTagProvider(view, controller)
                        updateMessageList(controller, mlist) {
                            adapter!!.notifyDataSetChanged()
                        }
                    }
                    view.tag_provider.addView(tv)
                }
            }
        }
    }

    fun updateParentMessage() {
        val controller = Controller(context!!)
        controller.getTagById(parentID) { tag ->
            if (tag.text != "#-1") {
                state = state or State.MESSAGE
                updateBackButton()
                controller.getMessageById(tag.text.slice(1 until tag.text.length).toLong()) {
                    view!!.current_message.visibility = View.VISIBLE
                    view!!.message_short.text = it.text.replace("\n", " ").replace(Regex("[ ]+"), " ")
                    val c = GregorianCalendar()
                    c.time = it.time
                    val dateFormat = SimpleDateFormat("HH:mm")
                    view!!.message_time.text = dateFormat.format(it.time)
                    view!!.close_message_button.setOnClickListener {
                        parentID = parentStack.pop()
                        updateMessageList(Controller(context!!), tagList) {
                            adapter!!.notifyDataSetChanged()
                        }
                        updateParentMessage()
                        if (parentStack.empty())
                            state = state xor State.MESSAGE
                        updateBackButton()
                    }
                }
            } else {
                view!!.current_message.visibility = View.GONE
                updateBackButton()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    lateinit var tagsCard: View
    lateinit var tagsRecycle: RecyclerView
    lateinit var editSearch: EditText

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.main_chat_fragment, container, false)
        //tagList.addAll(systemTagList)

        val controller = Controller(context!!)
        controller.addTag(Tag(123, "#-1", Tag.PARENT_TYPE)) {
            parentID = it
        }

        tagsRecycle = view.tags_search_recycler
        tagsCard = view.search_view
        editSearch = view.search_edit_text
        adapter = MessageViewAdapter(messageList, {
            val _id = it.tag.toString().toLong()
            tagList.add(_id)
            updateTagProvider(view, controller)
            updateMessageList(controller, tagList) {
                adapter!!.notifyDataSetChanged()
                view.message_edit_text.text.clear()
                view.message_list_1.smoothScrollToPosition(adapter!!.itemCount - 1)
            }
        }) {
            val _id = it.tag.toString().toLong()
            controller.getParentTag(_id) {
                if (parentStack.isNotEmpty() && parentStack.peek() != parentID) {
                    parentStack.push(parentID)
                } else if (parentStack.isEmpty()) {
                    parentStack.push(parentID)
                }
                parentID = it
                controller.getTagById(parentID) {
                    Log.d("ChatNote", "clicked ${it._id}")
                }
                updateParentMessage()
                updateTagProvider(view, controller)
                updateMessageList(controller, tagList) {
                    adapter!!.notifyDataSetChanged()
                    view.message_edit_text.text.clear()
                    view.message_list_1.smoothScrollToPosition(adapter!!.itemCount - 1)
                }
            }
            //val bundle = bundleOf("messageId" to _id)

            //Navigation.findNavController(view).navigate(R.id.action_mainChatFragment_to_messageFragment,bundle)
        }
        linearLayoutManager = LinearLayoutManager(context!!.applicationContext)
        linearLayoutManager.stackFromEnd = true
        view.message_list_1.layoutManager = linearLayoutManager
        view.message_list_1.adapter = adapter

        val attachmentList = mutableListOf<Attachment>()
        view.send_button.setOnClickListener {
            if (sendStats) {
                if (view.message_edit_text.text.isNotEmpty() || attachmentList.isNotEmpty()) {
                    val log = Logic(view.message_edit_text.text.toString()).getTags()
                    val tags = MutableList(log.size) { Tag(123, log[it], Tag.USER_TYPE) }
                    controller.getTagById(parentID) { pTag ->
                        tags.add(Tag(123, "${pTag.text}", Tag.PARENT_TYPE))
                        Log.d("DEBUG2", pTag.text)
                        controller.sendMessage(
                            SQL_Message(
                                123,
                                view.message_edit_text.text.toString(),
                                Date().time,
                                SQL_Message.USER_TYPE
                            ), tags, attachmentList, context!!
                        ) { mId ->
                            controller.addTag(Tag(123, "#$mId", Tag.PARENT_TYPE)) {
                                controller.addLink(Link(123, mId, it))
                            }
                            updateMessageList(controller) {
                                adapter!!.notifyDataSetChanged()
                                view.message_edit_text.text.clear()
                                view.message_list_1.smoothScrollToPosition(adapter!!.itemCount - 1)
                                attachmentList.clear()
                                view.attachments_view.removeAllViews()
                            }
                        }
                    }
                }
            } else {
                if (view.message_edit_text.text.isNotEmpty() || attachmentList.isNotEmpty()) {
                    sendStats = true
                    val log = Logic(view.message_edit_text.text.toString()).getTags()
                    val tags = MutableList(log.size) { Tag(123, log[it], Tag.USER_TYPE) }
                    controller.getTagById(parentID) { pTag ->
                        tags.add(Tag(editableMessageId, "${pTag.text}", Tag.PARENT_TYPE))
                        Log.d("DEBUG2", pTag.text)
                        controller.getMessageById(editableMessageId) {
                            controller.updateMessage(
                                SQL_Message(
                                    editableMessageId,
                                    view.message_edit_text.text.toString(),
                                    it.time.time,
                                    SQL_Message.USER_TYPE
                                ), tags, attachmentList, context!!
                            ) { mId ->
                                controller.addTag(Tag(123, "#$mId", Tag.PARENT_TYPE)) {
                                    controller.addLink(Link(123, mId, it))
                                }
                                updateMessageList(controller) {
                                    adapter!!.notifyDataSetChanged()
                                    view.message_edit_text.text.clear()
                                    view.message_list_1.smoothScrollToPosition(adapter!!.itemCount - 1)
                                    attachmentList.clear()
                                    view.attachments_view.removeAllViews()
                                }
                            }
                        }
                    }
                }
            }
        }
        (activity as MainActivity).onMessageDelete = {
            updateMessageList(controller) {
                adapter!!.notifyDataSetChanged()
            }
        }

        (activity as MainActivity).onMessageUpdate = {
            sendStats = false
            editableMessageId = it
            controller.getMessageById(it) { message ->
                view.message_edit_text.setText(message.text)
                for (i in message.attachment) {
                    attachmentList.clear()
                    controller.getAttachmentById(i) { attachment ->
                        attachmentList.add(attachment)
                        val v = AttachmentView(context!!, attachment)
                        v.delete_attachment.setOnClickListener {
                            v.visibility = View.GONE
                            attachmentList.remove(attachment)
                        }
                        view.attachments_view.addView(v)
                    }
                }
            }
        }
        (activity as MainActivity).onFragmentBackPressed = {
            Log.d("State1", state.toString())
            when {
                state and State.SEARCH > 0 -> changeSearchState()
                state and State.BOOKMARK > 0 -> controller.addTag(Tag(123, "#bookmark", Tag.SYSTEM_TYPE)) {
                    val mList = tagList.toMutableList()
                    mList.remove(it)
                    tagList = mList.toMutableSet()
                    updateTagProvider(view!!, controller)
                    updateMessageList(controller, mList) {
                        adapter!!.notifyDataSetChanged()
                    }
                    state = state xor State.BOOKMARK
                }
                state and State.MESSAGE_DETAILS > 0 -> {
                    Navigation.findNavController(activity as MainActivity, R.id.fragment).navigateUp()
                    state = state xor State.MESSAGE_DETAILS
                }
                state and State.MESSAGE > 0 -> {
                    parentID = parentStack.pop()
                    updateMessageList(Controller(context!!), tagList) {
                        adapter!!.notifyDataSetChanged()
                    }
                    updateParentMessage()
                    if (parentStack.empty())
                        state = state xor State.MESSAGE
                }
            }
            updateBackButton()
//            if(parentStack.isNotEmpty()) {
//                parentID = parentStack.pop()
//                updateMessageList(Controller(context!!), tagList) {
//                    adapter!!.notifyDataSetChanged()
//                }
//                updateParentMessage()
//            }
        }
        (activity as MainActivity).moveToMessageDetails = {
            state = state or State.MESSAGE_DETAILS
            updateBackButton()
        }

        view.attach_button.setOnClickListener {
            (activity as MainActivity).openDrawer {
                it.add_image.setOnClickListener {
                    ImageAttachment.sendIntentToPick(activity!!)
                    (activity as MainActivity).closeDrawer()
                }
                it.add_event.setOnClickListener {
                    val today = Calendar.getInstance()
                    DatePickerDialog(
                        context!!,
                        DatePickerDialog.OnDateSetListener { _, y, m, d ->
                            today[Calendar.YEAR] = y
                            today[Calendar.MONTH] = m
                            today[Calendar.DAY_OF_MONTH] = d
                            TimePickerDialog(
                                context,
                                TimePickerDialog.OnTimeSetListener { _, hh, mm ->
                                    today[Calendar.HOUR_OF_DAY] = hh
                                    today[Calendar.MINUTE] = mm
                                    val attach = NotificationAttachment.create(Notification(today.timeInMillis))
                                    attachmentList.add(attach)
                                    val v = AttachmentView(context!!, attach)
                                    v.delete_attachment.setOnClickListener {
                                        v.visibility = View.GONE
                                        attachmentList.remove(attach)
                                    }
                                    view.attachments_view.addView(v)
                                    (activity as MainActivity).closeDrawer()
                                },
                                today[Calendar.HOUR_OF_DAY],
                                today[Calendar.MINUTE],
                                true
                            ).show()
                        },
                        today[Calendar.YEAR],
                        today[Calendar.MONTH],
                        today[Calendar.DAY_OF_MONTH]
                    ).show()
                }
            }
        }


        view.search_view_back.setOnClickListener {
            changeSearchState()
        }
        updateMessageList(controller) {
            adapter!!.notifyDataSetChanged()
            view.message_list_1.smoothScrollToPosition(adapter!!.itemCount - 1)
        }



        (activity as MainActivity).onFragmentResult = { requestCode, resultCode, data ->
            if (requestCode == MainActivity.PIC_IMAGE_REQUEST) {
                val uri = data!!.data
                val attach = Attachment(
                    123, Attachment.IMAGE_TYPE.toString(),
                    Klaxon().toJsonString(
                        Image(
                            MainActivity.getRealPathFromUri(
                                context!!,
                                uri as Uri
                            )
                        )
                    ), 123
                )
                attachmentList.add(attach)
                val v = AttachmentView(context!!, attach)
                v.delete_attachment.setOnClickListener {
                    v.visibility = View.GONE
                    attachmentList.remove(attach)
                }
                view.attachments_view.addView(v)
            }
        }


        return view
    }

    fun onSearchVisible() {
        val controller = Controller(context!!)
        val messageList = mutableListOf<Long>()
        val tagList = mutableListOf<Long>()

        val tagAdapter = MessageRowAdapter(messageList) {
            val _id = it.tag.toString().toLong()
            val bundle = bundleOf("messageId" to _id)
            Navigation.findNavController(it).navigate(R.id.action_mainChatFragment_to_messageFragment, bundle)
            state = state or State.MESSAGE_DETAILS
            changeSearchState()
        }

        val linearTagLayoutManager = LinearLayoutManager(context!!.applicationContext)
        search_message_recycler.layoutManager = linearTagLayoutManager
        search_message_recycler.adapter = tagAdapter
        controller.getMessageByMessagePatr(search_edit_text.text.toString()) { exit ->
            val a = mutableSetOf<Long>()
            a.addAll(exit)

            messageList.clear()
            messageList.addAll(a)
            tagAdapter.notifyDataSetChanged()
        }
        if (search_edit_text.text.toString().isEmpty()) {
            messages_card.visibility = View.GONE
        } else {
            messages_card.visibility = View.VISIBLE
        }
        search_edit_text.afterTextChanged {
            messages_card.visibility = View.VISIBLE
            controller.getMessageByMessagePatr(it) { exit ->
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
    var date = Calendar.getInstance()

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.search_button) {
            changeSearchState()
            return true
        }
        if (item.itemId == R.id.search_by_date_button) {
            DatePickerDialog(
                context!!, d,
                date.get(Calendar.YEAR),
                date.get(Calendar.MONTH),
                date.get(Calendar.DAY_OF_MONTH)
            )
                .show()
        }
        if (item.itemId == R.id.bookmarks) {
            val controller = Controller(context!!)
            controller.addTag(Tag(123, "#bookmark", Tag.SYSTEM_TYPE)) {
                tagList.clear()
                tagList.add(it)
                state = state or State.BOOKMARK
                updateBackButton()
                updateTagProvider(view!!, Controller(context!!))
                updateMessageList(controller, tagList) {
                    adapter!!.notifyDataSetChanged()
                }

            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setInitialDateTime(date: Calendar) {
        Log.d("WORK", date.toString())
    }

    val d: DatePickerDialog.OnDateSetListener =
        DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            val date = Calendar.getInstance()
            date.set(Calendar.YEAR, year)
            date.set(Calendar.MONTH, monthOfYear)
            date.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            setInitialDateTime(date)
        }

    private fun changeSearchState() {
        val status = when (searchVisible) {
            true -> {
                state = state xor State.SEARCH
                View.GONE
            }
            false -> {
                search_edit_text.requestFocus()
                (context!!.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager)
                    .toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0)
                state = state or State.SEARCH
                View.VISIBLE
            }
        }
        updateBackButton()
        tagsCard.visibility = status

        searchVisible = !searchVisible
        if (searchVisible) {
            onSearchVisible()
        }
    }
}