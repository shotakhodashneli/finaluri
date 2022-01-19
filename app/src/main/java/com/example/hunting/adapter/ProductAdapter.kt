package com.example.hunting.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.hunting.R
import com.example.hunting.model.Product
import com.google.android.material.card.MaterialCardView

class ProductAdapter(private val productList: ArrayList<Product>, val listener: OnItemClickListener):
    RecyclerView.Adapter<ProductAdapter.ViewHolder>() {

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view), View.OnClickListener {
        val productName: TextView = view.findViewById(R.id.productTitle)
        val productPrice: TextView = view.findViewById(R.id.productPrice)
        val productDescription: TextView = view.findViewById(R.id.productDescription)

        private val item: MaterialCardView = view.findViewById(R.id.item)

        init {
            item.findViewById<ImageView>(R.id.cart).setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onItemClick(position)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_shop, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentProduct = productList[position]

        holder.productName.text = currentProduct.name
        holder.productDescription.text = currentProduct.description
        holder.productPrice.text = currentProduct.price.toString() + "$"

    }

    override fun getItemCount() = productList.size

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

}