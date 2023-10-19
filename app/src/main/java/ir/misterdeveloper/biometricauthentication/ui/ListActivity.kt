package ir.misterdeveloper.biometricauthentication.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ir.misterdeveloper.biometricauthentication.R

import android.content.Intent
import android.view.View

import ir.misterdeveloper.biometricauthentication.adapter.MessageListAdapter
import ir.misterdeveloper.biometricauthentication.common.EncryptedMessage
import ir.misterdeveloper.biometricauthentication.databinding.ActivityListBinding
import ir.misterdeveloper.biometricauthentication.util.PreferenceUtil


class ListActivity : AppCompatActivity(), MessageListAdapter.OnItemClickListener {

    lateinit var binding: ActivityListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

       binding. fab.setOnClickListener {
           startActivity(Intent(this, EncryptionActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()

        // Set Adapter
        showMessageList()
    }

    override fun onItemClick(item: EncryptedMessage, itemView: View) {
        val decryptionIntent = (Intent(this, DecryptionActivity::class.java))
        decryptionIntent.putExtra(getString(R.string.parcel_message), item)
        startActivity(decryptionIntent)
    }

    private fun showMessageList() {
        val messageList = PreferenceUtil.getMessageList(applicationContext)
        if (!messageList.isNullOrEmpty()) {
            binding.include.textViewNoMessage.visibility = View.GONE
            binding.include.recyclerView.adapter = MessageListAdapter(messageList, this)
        }
    }

}
