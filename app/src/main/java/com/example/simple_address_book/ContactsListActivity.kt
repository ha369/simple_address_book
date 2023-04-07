package com.example.simple_address_book

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ContactsListActivity : AppCompatActivity() {
    private var contactsList = ArrayList<Contact>()
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ContactsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contacts_list)

        contactsList = ArrayList()
        // 添加一些样例数据
        contactsList.add(Contact("张三", "123456789", "北京市海淀区"))
        contactsList.add(Contact("李四", "178276049", "上海市浦东新区"))
        contactsList.add(Contact("王五", "888888888", "深圳市福田区"))

        recyclerView = findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = ContactsAdapter(this, contactsList, object : ContactsAdapter.OnItemClickListener {
            override fun onEditClick(position: Int) {
                // 点击编辑按钮，跳转到编辑页面
                editContact(position)
            }

            override fun onDeleteClick(position: Int) {
                // 点击删除按钮，弹出确认对话框
                showDeleteDialog(position)
            }
        })
        recyclerView.adapter = adapter

        // 添加新联系人按钮
        val addButton = findViewById<Button>(R.id.add_button)
        addButton.setOnClickListener {
            addContact()
        }
    }

    private val addContactLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            val contact = data?.getParcelableExtra<Contact>("contact")
            if (contact != null) {
                contactsList.add(contact!!)
                adapter.notifyItemInserted(contactsList.size - 1)
            }
        }
    }

    private val editContactLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            val contact = data?.getParcelableExtra<Contact>("contact")
            val position = data!!.getIntExtra("position", -1)
            if (contact != null && position != -1) {
                contactsList[position] = contact
                adapter.notifyItemChanged(position)
            }
        }
    }

    private fun addContact() {
        val intent = Intent(this, EditContactActivity::class.java).apply {
            putExtra("mode", "add")
        }
        addContactLauncher.launch(intent)
    }

    private fun editContact(position: Int) {
        val contact = contactsList[position]
        val intent = Intent(this, EditContactActivity::class.java).apply {
            putExtra("mode", "edit")
            putExtra("contact", contact)
            putExtra("position", position)
        }
        editContactLauncher.launch(intent)
    }

    private fun showDeleteDialog(position: Int) {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("是否删除该联系人？")
            .setPositiveButton("确认") { _, _ ->
                deleteContact(position)
            }
            .setNegativeButton("取消", null)
        builder.create().show()
    }

    private fun deleteContact(position: Int) {
        contactsList.removeAt(position)
        adapter.notifyItemRemoved(position)
    }
}
