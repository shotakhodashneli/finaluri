package com.example.hunting.fragments

import android.os.Bundle
import android.renderscript.Sampler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hunting.R
import com.example.hunting.adapter.ProductAdapter
import com.example.hunting.databinding.FragmentCartBinding
import com.example.hunting.model.Product
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class CartFragment: Fragment (R.layout.fragment_cart), ProductAdapter.OnItemClickListener {

    private var _binding: FragmentCartBinding? = null
    private val binding get() = _binding!!

    private val db = FirebaseDatabase.getInstance().getReference("users")
    private val dbProducts = FirebaseDatabase.getInstance().getReference("products")
    private val auth = FirebaseAuth.getInstance()
    private lateinit var recyclerViewCartProducts: RecyclerView
    private lateinit var arrayListCartProducts: ArrayList<Product>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerViewCartProducts = view.findViewById(R.id.recyclerViewCartProducts)

        recyclerViewCartProducts.layoutManager = LinearLayoutManager(activity)

        arrayListCartProducts = arrayListOf()
        loadCartProducts()
    }

    private fun loadCartProducts() {
        val list = arrayListOf<String>()
        db.child(auth.currentUser!!.uid).child("cart").addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (snap in snapshot.children) {
                        val currentItem = snap.value.toString()
                        list.add(currentItem)
                    }
                    recyclerViewCartProducts.adapter = ProductAdapter(requireContext(), arrayListCartProducts, this@CartFragment)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

        dbProducts.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (snap in snapshot.children) {
                        if (list.contains(snap.key.toString())) {
                            val currentItem = snap.getValue(Product::class.java)?: return
                            arrayListCartProducts.add(currentItem)
                        }
                    }
                    recyclerViewCartProducts.adapter = ProductAdapter(requireContext(), arrayListCartProducts, this@CartFragment)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

    }

    override fun onItemClick(position: Int) {
        Log.d("SHOW", "")
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}