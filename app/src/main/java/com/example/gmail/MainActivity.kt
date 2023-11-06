package com.example.gmail

import android.content.res.ColorStateList
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import java.text.SimpleDateFormat
import java.util.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gmail.databinding.ActivityMainBinding
import com.example.gmail.databinding.GmailItemBinding
import com.example.gmail.databinding.ActivityMainRecyclerviewBinding

class MainActivity : AppCompatActivity() {

    private lateinit var main: ActivityMainBinding
    private lateinit var recyclerMain: ActivityMainRecyclerviewBinding

    data class Email(val sender: String, val subject: String, val timestamp: String, val color: Int)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        main = ActivityMainBinding.inflate(layoutInflater)
        recyclerMain = ActivityMainRecyclerviewBinding.inflate(layoutInflater)

//        val view = main.root
        val view = recyclerMain.root
        setContentView(view)

        val data = generateRandomEmailDataList(25)

//        val adapter = EmailAdapter(this, data)
//        val listView = main.listView
//        listView.adapter = adapter

        val adapter = EmailAdapterRecyclerView(this, data)
        val recyclerView = recyclerMain.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }

    inner class EmailAdapterRecyclerView(private val context: AppCompatActivity, private val emails: List<Email>) :
        RecyclerView.Adapter<EmailAdapterRecyclerView.EmailViewHolder>() {

        inner class EmailViewHolder(val binding: GmailItemBinding) :
            RecyclerView.ViewHolder(binding.root)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmailViewHolder {
            val binding =
                GmailItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return EmailViewHolder(binding)
        }

        @RequiresApi(Build.VERSION_CODES.M)
        override fun onBindViewHolder(holder: EmailViewHolder, position: Int) {
            val email = emails[position]
            with(holder.binding) {
                sender.text = email.sender
                firstSender.text = email.sender.take(1)
                timestamp.text = email.timestamp
                content.text = email.subject
                val randomColor = ContextCompat.getColor(context, generateRandomColor())
                icon.foregroundTintList = ColorStateList.valueOf(randomColor)
            }
        }

        override fun getItemCount(): Int {
            return emails.size
        }
    }

    inner class EmailAdapter(context: AppCompatActivity, objects: List<Email>) :
        ArrayAdapter<Email>(context, 0, objects) {

        @RequiresApi(Build.VERSION_CODES.M)
        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val binding: GmailItemBinding

            if (convertView == null) {
                binding = GmailItemBinding.inflate(LayoutInflater.from(context), parent, false)
                binding.root.tag = binding
            } else {
                binding = convertView.tag as GmailItemBinding
            }

            val email = getItem(position)

            binding.sender.text = email?.sender
            binding.firstSender.text = email?.sender?.take(1)
            binding.timestamp.text = email?.timestamp
            binding.content.text = email?.subject

            val randomColor = ContextCompat.getColor(context, generateRandomColor())
            val icon = binding.icon
            icon.foregroundTintList = ColorStateList.valueOf(randomColor)

            return binding.root
        }
    }

    private fun generateRandomEmailData(): Email {
        val random = Random()

        val sender = generateRandomSender()
        val subject = generateRandomSubject(200);

        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR, random.nextInt(12) + 1)
        calendar.set(Calendar.MINUTE, random.nextInt(60))

        val timestamp = SimpleDateFormat("hh:mm a", Locale.US).format(calendar.time)

        val randomColor = generateRandomColor()
        return Email(sender, subject, timestamp, randomColor)
    }

    private fun generateRandomEmailDataList(count: Int): List<Email> {
        val emailList = mutableListOf<Email>()
        repeat(count) {
            emailList.add(generateRandomEmailData())
        }
        return emailList
    }

    private fun generateRandomSubject(subjectLength: Int): String {
        val letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"
        val random = Random()

        val subject = StringBuilder(subjectLength)
        repeat(subjectLength) {
            val randomIndex = random.nextInt(letters.length)
            val randomChar = letters[randomIndex]
            subject.append(randomChar)
        }

        return subject.toString()
    }

    private fun generateRandomSender(): String {
        val random = Random()

        return senderNames[random.nextInt(senderNames.size)]
    }

    private fun generateRandomColor(): Int {
        val random = Random()

        return colorResourceIds[random.nextInt(colorResourceIds.size)]
    }


    private val senderNames = arrayOf(
        "Alice", "Bob", "Charlie", "David", "Eve",
        "Frank", "Grace", "Hannah", "Isaac", "Jane"
    )

    private val colorResourceIds = arrayOf(
        R.color.blue,
        R.color.olive,
        R.color.purple,
        R.color.brown,
        R.color.navy,
        R.color.teal
    )

}
