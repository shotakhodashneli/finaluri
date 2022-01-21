package com.example.hunting.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hunting.R
import com.example.hunting.adapter.ProductAdapter
import com.example.hunting.databinding.FragmentShopBinding
import com.example.hunting.model.Product
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ShopFragment: Fragment (R.layout.fragment_shop), ProductAdapter.OnItemClickListener {

    private var _binding: FragmentShopBinding? = null
    private val binding get() = _binding!!

    private val dbProducts = FirebaseDatabase.getInstance().getReference("products")
    private val dbUsers = FirebaseDatabase.getInstance().getReference("users")
    private val auth = FirebaseAuth.getInstance()

    private lateinit var recyclerViewProducts: RecyclerView
    private lateinit var arrayListProducts: ArrayList<Product>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentShopBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        loadProducts()
    }

    private fun init() {
        recyclerViewProducts = binding.recyclerViewProducts

        val layoutManager = LinearLayoutManager(activity)
        layoutManager.reverseLayout = true
        layoutManager.stackFromEnd = true

        recyclerViewProducts.layoutManager = layoutManager

        arrayListProducts = arrayListOf()

    }

    private fun loadProducts() {

        dbProducts.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {

                    arrayListProducts.clear()

                    for (snap in snapshot.children) {
                        val currentProduct = snap.getValue(Product::class.java)?: return
                        arrayListProducts.add(currentProduct)
                    }
                    recyclerViewProducts.adapter = ProductAdapter(requireContext(), arrayListProducts, this@ShopFragment)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onItemClick(position: Int) {
        val item = arrayListProducts[position]
        val itemId = item.id

        dbUsers.child(auth.currentUser!!.uid).child("cart").addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                val list: ArrayList<String> = arrayListOf()

                for (snap in snapshot.children) {
                    val id = snap.getValue(String::class.java)?: return
                    list.add(id)
                }

                if (!list.contains(itemId.toString())) {
                    val id = snapshot.ref.push().key
                    snapshot.ref.child(id.toString()).setValue(itemId)
                    Toast.makeText(activity, "პროდუქტი დაემატა კალათაში.", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(activity, "პროდუქტი უკვე დამატებულია კალათაში.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

//        val id = dbUsers.child(auth.currentUser!!.uid).child("cart").push().key
//        dbUsers.child(auth.currentUser!!.uid).child("cart").child(id.toString()).setValue(itemId)
    }

}