package com.example.demo

import android.app.ProgressDialog
import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.demo.AsyncTasks.GistDetailAsyncTask
import com.example.demo.AsyncTasks.GistDetailAsyncTaskInterface
import com.example.demo.Dao.GistDetailDB
import com.example.demo.Database.DbWorkerThread
import com.example.demo.Database.GistDetailsDatabase
import com.example.demo.Objects.Gist
import com.example.demo.Objects.GistDetail
import com.example.demo.Objects.Owner
import com.squareup.picasso.Picasso

class DetailActivity : AppCompatActivity(), GistDetailAsyncTaskInterface {


    var progressDialog: ProgressDialog? = null
    var gist: Gist? = null
    var gistDetail: GistDetail? = null
    var gistDetailDB: GistDetailDB? = null
    private val mUiHandler = Handler()


    private var mDb: GistDetailsDatabase? = null
    private lateinit var mDbWorkerThread: DbWorkerThread


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detail_activity_layout)
        val window: Window = this.window
        supportActionBar?.hide()
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        gist = intent.getSerializableExtra("Gist") as? Gist

        mDbWorkerThread = DbWorkerThread("dbWorkerThread")
        mDbWorkerThread.start()

        mDb = GistDetailsDatabase.getInstance(this)

        val backImageView: ImageView = findViewById(R.id.detail_back_image_view)
        backImageView.setOnClickListener {
            onBackImageViewClicked()
        }

        progressDialog = ProgressDialog(this)
        progressDialog!!.setMessage(resources.getString(R.string.loading))
        progressDialog!!.show()

        if (checkNetworkAvailability()) {
            var publicGistsAsyncTask: GistDetailAsyncTask = GistDetailAsyncTask(this, this.gist!!)
            publicGistsAsyncTask.execute()
        } else {
            val task = Runnable {
                var gistDetailDBList = mDb?.gistDetailDBDao()?.getGist(gist!!.id)
                if (gistDetailDBList != null) {
                    if (gistDetailDBList.isEmpty()) {
                        Toast.makeText(
                            this,
                            resources.getText(R.string.detailToastMessage),
                            Toast.LENGTH_SHORT
                        ).show()
                        progressDialog!!.dismiss()
                    } else {
                        var gistDetailDB = gistDetailDBList.get(0)
                        var gistDetail = GistDetail()
                        gistDetail.id = gistDetailDB.id
                        gistDetail.url = gistDetailDB.url
                        gistDetail.forks_url = gistDetailDB.forks_url
                        gistDetail.node_id = gistDetailDB.node_id
                        gistDetail.git_pull_url = gistDetailDB.git_pull_url
                        gistDetail.git_push_url = gistDetailDB.git_push_url
                        gistDetail.html_url = gistDetailDB.html_url
                        gistDetail.created_at = gistDetailDB.created_at
                        gistDetail.updated_at = gistDetailDB.updated_at
                        gistDetail.comments = gistDetailDB.comments
                        gistDetail.comments_url = gistDetailDB.comments_url
                        gistDetail.description = gistDetailDB.description

                        var owner = Owner()
                        owner.login = gistDetailDB.owner_login
                        owner.avatar_url = gistDetailDB.owner_avatar_url
                        owner.id = gistDetailDB.owner_id
                        owner.node_id = gistDetailDB.owner_node_id
                        owner.html_url = gistDetailDB.owner_html_url
                        owner.type = gistDetailDB.owner_type
                        owner.site_admin = gistDetailDB.owner_site_admin

                        gistDetail.owner = owner
                        mUiHandler.post({
                            generateViews(gistDetail)
                        })

                    }
                }
            }
            mDbWorkerThread.postTask(task)
            progressDialog!!.dismiss()
        }

    }

    override fun gistDetailAsyncTaskFinished(gistDetail: GistDetail) {

        val gistDetailDB2 = GistDetailDB(
            gistDetail.id,
            gistDetail.url,
            gistDetail.forks_url,
            gistDetail.node_id,
            gistDetail.git_pull_url,
            gistDetail.git_push_url,
            gistDetail.html_url,
            gistDetail.created_at,
            gistDetail.updated_at,
            gistDetail.comments,
            gistDetail.comments_url,
            gistDetail.description,
            gistDetail.owner!!.id,
            gistDetail.owner!!.avatar_url,
            gistDetail.owner!!.login,
            gistDetail.owner!!.node_id,
            gistDetail.owner!!.html_url,
            gistDetail.owner!!.type,
            gistDetail.owner!!.site_admin
        )

        val task = Runnable { mDb?.gistDetailDBDao()?.insert(gistDetailDB2) }
        mDbWorkerThread.postTask(task)

        generateViews(gistDetail)
        progressDialog!!.dismiss()

    }


    fun generateViews(gistDetail: GistDetail) {
        val userImageView: ImageView = findViewById(R.id.details_image_view)
        Picasso.get().load(gist!!.owner!!.avatar_url).into(userImageView)

        val urlTextView: TextView = findViewById(R.id.url_text_view)
        urlTextView.setText("URL : " + gistDetail.url)

        val forksUrlTextView: TextView = findViewById(R.id.forks_url_textView)
        forksUrlTextView.setText("Forks URL : " + gistDetail.forks_url)

        val nodeIdTextView: TextView = findViewById(R.id.node_id_textView)
        nodeIdTextView.setText("Node Id : " + gistDetail.node_id)

        val idTextView: TextView = findViewById(R.id.id_textView)
        idTextView.setText("ID : " + gistDetail.id)

        val gitPullUrlTextView: TextView = findViewById(R.id.git_pull_url_textView)
        gitPullUrlTextView.setText("Git Pull URL : " + gistDetail.git_pull_url)

        val gitPushUrlTextView: TextView = findViewById(R.id.git_push_url_textView)
        gitPushUrlTextView.setText("Git Push URL : " + gistDetail.git_push_url)

        val htmlUrlTextView: TextView = findViewById(R.id.html_url_textView)
        htmlUrlTextView.setText("HTML URL : " + gistDetail.html_url)

        val publicTextView: TextView = findViewById(R.id.public_textView)
        if (gistDetail.public) {
            publicTextView.setText("Public : True")
        } else {
            publicTextView.setText("Public : False")
        }

        val createdAtTextView: TextView = findViewById(R.id.created_at_textView)
        createdAtTextView.setText("Create At : " + gistDetail.created_at)

        val updatedAtTextView: TextView = findViewById(R.id.updated_at_textView)
        updatedAtTextView.setText("Updated At : " + gistDetail.updated_at)

        val descriptionTextView: TextView = findViewById(R.id.description_textView)
        descriptionTextView.setText("Description : " + gistDetail.description)

        val commentsTextView: TextView = findViewById(R.id.comments_textView)
        commentsTextView.setText("Comment Amount : " + gistDetail.comments.toString())

        val commentsUrlTextView: TextView = findViewById(R.id.comments_url_textView)
        commentsUrlTextView.setText("Comment URL : " + gistDetail.comments_url)

        val ownerLoginTextView: TextView = findViewById(R.id.owner_login_textView)
        ownerLoginTextView.setText("Login : " + gistDetail.owner!!.login)

        val ownerIdTextView: TextView = findViewById(R.id.owner_id_textView)
        ownerIdTextView.setText("ID : " + gistDetail.owner!!.id.toString())

        val ownerNodeIdTextView: TextView = findViewById(R.id.owner_node_id_textView)
        ownerNodeIdTextView.setText("Node ID : " + gistDetail.owner!!.node_id)

        val ownerHtmlUrlTextView: TextView = findViewById(R.id.owner_html_url_textView)
        ownerHtmlUrlTextView.setText("HTML URL : " + gistDetail.owner!!.html_url)

        val ownerTypeTextView: TextView = findViewById(R.id.owner_type_textView)
        ownerTypeTextView.setText("Type : " + gistDetail.owner!!.type)

        val siteAdminTextView: TextView = findViewById(R.id.site_admin_textView)

        if (gistDetail.owner!!.site_admin) {
            siteAdminTextView.setText("Site Admin : True")
        } else {
            siteAdminTextView.setText("Site Admin : False")
        }
    }

    override fun gistDetailAsyncTaskFailed() {
        Toast.makeText(this, resources.getText(R.string.detailToastMessage), Toast.LENGTH_SHORT)
            .show()
        progressDialog!!.dismiss()
    }

    fun onBackImageViewClicked() {
        this.finish()
    }

    fun checkNetworkAvailability(): Boolean {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }

    override fun onDestroy() {
        GistDetailsDatabase.destroyInstance()
        mDbWorkerThread.quit()
        super.onDestroy()
    }
}