package com.example.demo

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Window
import android.view.WindowManager
import android.widget.ListAdapter
import android.widget.ListView
import android.widget.Toast
import com.example.demo.Adapters.GistsListAdapter
import com.example.demo.Adapters.ListAdapterInterface
import com.example.demo.AsyncTasks.PublicGistsAsyncTask
import com.example.demo.AsyncTasks.PublicGistsAsyncTaskInterface
import com.example.demo.Objects.Gist

class ListActivity : AppCompatActivity(), PublicGistsAsyncTaskInterface, ListAdapterInterface {


    var progressDialog : ProgressDialog? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.list_activity_layout)
        val window : Window = this.window
        supportActionBar?.hide()
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)

        progressDialog = ProgressDialog(this)
        progressDialog!!.setMessage(resources.getString(R.string.loading))
        progressDialog!!.show()
        var publicGistsAsyncTask: PublicGistsAsyncTask = PublicGistsAsyncTask(this)
        publicGistsAsyncTask.execute()
    }


    override fun publicGistsAsyncTaskFinished(gists: Array<Gist>) {
        progressDialog!!.dismiss()
        var listView : ListView = findViewById(R.id.list_view)
        var listAdapter = GistsListAdapter(gists, this, this)
        listView.adapter = listAdapter
        listAdapter.notifyDataSetChanged()
    }

    override fun publicGistsAsyncTaskFailed() {
        Toast.makeText(this, resources.getText(R.string.toastMessage), Toast.LENGTH_SHORT).show()
        progressDialog!!.dismiss()
    }


    override fun gistsDetail(gist: Gist) {
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra("Gist", gist)
        startActivity(intent)
    }

}