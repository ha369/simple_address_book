package com.example.simple_address_book

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class EditContactActivity : AppCompatActivity() {
    private lateinit var nameEditText: EditText
    private lateinit var phoneEditText: EditText
    private lateinit var addressEditText: EditText
    private lateinit var saveButton: Button
    private lateinit var cancelButton: Button

    private var contact: Contact? = null
    private var position: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_contact)

        nameEditText = findViewById(R.id.name_edit_text)
        phoneEditText = findViewById(R.id.phone_edit_text)
        addressEditText = findViewById(R.id.address_edit_text)
        saveButton = findViewById(R.id.save_button)
        cancelButton = findViewById(R.id.cancel_button)

        val mode = intent.getStringExtra("mode")
        if (mode == "edit") {
            // 编辑模式，显示原有通讯录信息
            contact = intent.getParcelableExtra("contact")
            position = intent.getIntExtra("position", -1)
            nameEditText.setText(contact?.name)
            phoneEditText.setText(contact?.phone)
            addressEditText.setText(contact?.address)
            saveButton.text = "更新"
        } else {
            // 新增模式，不显示原有通讯录信息
            saveButton.text = "新增"
        }

        saveButton.setOnClickListener {
            val name = nameEditText.text.toString()
            val phone = phoneEditText.text.toString()
            val address = addressEditText.text.toString()

            if (name.isEmpty() || phone.isEmpty() || address.isEmpty()) {
                Toast.makeText(this, "请输入完整的通讯录信息", Toast.LENGTH_SHORT).show()
            } else {
                if (mode == "edit") {
                    // 更新通讯录信息
                    contact?.name = name
                    contact?.phone = phone
                    contact?.address = address
                    val resultIntent = Intent().apply {
                        putExtra("contact", contact)
                        putExtra("position", position)
                    }
                    setResult(Activity.RESULT_OK, resultIntent)
                    finish()
                } else {
                    // 新增通讯录信息
                    val newContact = Contact(name, phone, address)
                    val resultIntent = Intent().apply {
                        putExtra("contact", newContact)
                    }
                    setResult(Activity.RESULT_OK, resultIntent)
                    finish()
                }
            }
        }

        cancelButton.setOnClickListener {
            setResult(Activity.RESULT_CANCELED)
            finish()
        }
    }
}
