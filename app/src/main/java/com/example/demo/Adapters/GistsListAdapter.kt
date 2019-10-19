package com.example.demo.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.demo.Objects.Gist
import com.example.demo.R
import com.squareup.picasso.Picasso

interface ListAdapterInterface  {
    fun gistsDetail(gist: Gist)
}


class GistsListAdapter(gists: Array<Gist>, context: Context, listAdapterInterface: ListAdapterInterface) :
    ArrayAdapter<Gist>(context, R.layout.list_view_item_layout, gists ) {

    var listAdapterInterface: ListAdapterInterface? = listAdapterInterface
    private var gists: Array<Gist> = gists

    private class ListViewHolder {
        internal var itemImageView: ImageView? = null
        internal var itemIdTextView: TextView? = null
        internal var itemUserTextView: TextView? = null
        internal var itemDateTextView: TextView? = null
    }

    override fun getView(i: Int, view: View?, viewGroup: ViewGroup): View {
        var view = view
        val inflater = LayoutInflater.from(context)
        view = inflater.inflate(R.layout.list_view_item_layout, viewGroup, false)
        val viewHolder: ListViewHolder = ListViewHolder()
        var gist = getItem(i)

        viewHolder.itemImageView = view!!.findViewById<View>(R.id.list_item_image_view) as ImageView
        Picasso.get().load(gist.owner!!.avatar_url).into(viewHolder.itemImageView)

        viewHolder.itemIdTextView = view!!.findViewById<View>(R.id.list_item_id_text_view) as TextView
        viewHolder.itemIdTextView!!.setText(gist.id)

        viewHolder.itemUserTextView = view!!.findViewById<View>(R.id.list_item_user_text_view) as TextView
        viewHolder.itemUserTextView!!.setText(gist.owner?.login)

        viewHolder.itemDateTextView = view!!.findViewById<View>(R.id.list_item_date_text_view) as TextView
        val dateArray = gist.created_at.split("T")
        viewHolder.itemDateTextView!!.setText(dateArray[0])


        view.setOnClickListener{
            listAdapterInterface!!.gistsDetail(gist)
        }

        view.tag = viewHolder
        return view
    }
}

