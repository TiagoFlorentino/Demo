package com.example.demo

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.view.Window
import android.view.WindowManager
import android.widget.ListView
import android.widget.Toast
import com.example.demo.Adapters.GistsListAdapter
import com.example.demo.Adapters.ListAdapterInterface
import com.example.demo.AsyncTasks.PublicGistsAsyncTask
import com.example.demo.AsyncTasks.PublicGistsAsyncTaskInterface
import com.example.demo.Dao.GistDB
import com.example.demo.Database.DbWorkerThread
import com.example.demo.Database.GistDatabase
import com.example.demo.Objects.Gist
import com.example.demo.Objects.Owner

class ListActivity : AppCompatActivity(), PublicGistsAsyncTaskInterface, ListAdapterInterface {


    var progressDialog : ProgressDialog? = null
    private val mUiHandler = Handler()

    private lateinit var mDbWorkerThread: DbWorkerThread
    private var mDb: GistDatabase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.list_activity_layout)
        val window : Window = this.window
        supportActionBar?.hide()
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)

        mDbWorkerThread = DbWorkerThread("dbWorkerThread1")
        mDbWorkerThread.start()
        mDb = GistDatabase.getInstance(this)


        progressDialog = ProgressDialog(this)
        progressDialog!!.setMessage(resources.getString(R.string.loading))
        progressDialog!!.show()
        if(checkNetworkAvailability()){
            var publicGistsAsyncTask: PublicGistsAsyncTask = PublicGistsAsyncTask(this)
            publicGistsAsyncTask.execute()
        }
        else{
            val task = Runnable {
                var gistList = mDb?.gistDBDao()?.getAll()
                if (gistList != null) {
                    if(gistList.isEmpty()){
                        mUiHandler.post({
                            Toast.makeText(
                                this,
                                resources.getText(R.string.toastMessage),
                                Toast.LENGTH_SHORT
                            ).show()
                            progressDialog!!.dismiss()
                        })
                    }
                    else{
                        mUiHandler.post({
                            generateListView(gistList)
                            progressDialog!!.dismiss()
                        })
                    }
                } else {
                    mUiHandler.post({
                        Toast.makeText(
                            this,
                            resources.getText(R.string.toastMessage),
                            Toast.LENGTH_SHORT
                        ).show()
                        progressDialog!!.dismiss()
                    })
                }
            }
            mDbWorkerThread.postTask(task)
        }

    }

    fun generateListView(gists : List<GistDB>){
        var gistArray : Array<Gist> = emptyArray()
        for (gist in gists){
            var newGist = Gist()
            newGist.id = gist.id
            newGist.created_at = gist.created_at
            var newOwner = Owner()
            newOwner.id = gist.owner_id
            newOwner.site_admin = gist.owner_site_admin
            newOwner.type = gist.owner_type
            newOwner.html_url = gist.owner_html_url
            newOwner.login = gist.owner_login
            newOwner.avatar_url = gist.owner_avatar_url
            newOwner.node_id = gist.owner_node_id

            newGist.owner = newOwner
            gistArray += newGist
        }
        var listView : ListView = findViewById(R.id.list_view)
        var listAdapter = GistsListAdapter(gistArray, this, this)
        listView.adapter = listAdapter
        listAdapter.notifyDataSetChanged()
    }


    override fun publicGistsAsyncTaskFinished(gists: Array<Gist>) {

        val task = Runnable {
            for (gistItem in gists)
            {
                var gistDB = GistDB(gistItem.id,
                    gistItem.created_at,
                    gistItem.owner!!.id,
                    gistItem.owner!!.avatar_url,
                    gistItem.owner!!.login,
                    gistItem.owner!!.node_id,
                    gistItem.owner!!.html_url,
                    gistItem.owner!!.type,
                    gistItem.owner!!.site_admin
                    )
                mDb?.gistDBDao()?.insert(gistDB)
            }

        }
        mDbWorkerThread.postTask(task)


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

    override fun onDestroy() {
        GistDatabase.destroyInstance()
        mDbWorkerThread.quit()
        super.onDestroy()
    }

    fun checkNetworkAvailability(): Boolean {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }

}