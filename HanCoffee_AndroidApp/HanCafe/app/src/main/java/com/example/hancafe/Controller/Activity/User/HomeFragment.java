package com.example.hancafe.Controller.Activity.User;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hancafe.Controller.Activity.Adapter.CategoryAdapter;
import com.example.hancafe.Controller.Activity.Adapter.ProductsAdapter;
import com.example.hancafe.Controller.Observer.ApiDataObserver;
import com.example.hancafe.Model.CategoryProduct;
import com.example.hancafe.Model.Product;
import com.example.hancafe.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements CategoryAdapter.OnItemClickListener, ProductsAdapter.OnItemClickListener {

    private RecyclerView rvCategory, rvProduct;
    private ProductsAdapter productsAdapter;
    private CategoryAdapter categoryAdapter;
    private SearchView svSearch;
    private List<Product> products = new ArrayList<>();
    private List<CategoryProduct> categories = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        rvCategory = view.findViewById(R.id.rvCategory);
        rvProduct = view.findViewById(R.id.rvProduct);
        svSearch = view.findViewById(R.id.svSearch);
        setEvent();
        return view;
    }

    private void setEvent() {
        ApiDataObserver observer = new ApiDataObserver(this);
        observer.startListening();

        svSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                txtSearch(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                txtSearch(query);
                return false;
            }
        });
    }
    private void txtSearch(String query) {
        List<Product> filteredProducts = new ArrayList<>();
        for (Product product : products) {
            if (product.getName().toLowerCase().contains(query.toLowerCase())) {
                filteredProducts.add(product);
            }
        }
        productsAdapter = new ProductsAdapter(filteredProducts);
        rvProduct.setAdapter(productsAdapter);
        productsAdapter.notifyDataSetChanged();
    }

    public void updateCategories(List<CategoryProduct> categories) {
        this.categories = categories;
        updateCategoryUI();
    }

    public void updateProducts(List<Product> products) {
        this.products = products;
        updateProductUI();
    }

    private void updateCategoryUI() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        rvCategory.setLayoutManager(linearLayoutManager);
        categoryAdapter = new CategoryAdapter(categories);
        rvCategory.setAdapter(categoryAdapter);
        categoryAdapter.setOnItemCategoryClickListener(this);
    }

    private void updateProductUI() {
        LinearLayoutManager linearLayoutManagerProduct = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rvProduct.setLayoutManager(linearLayoutManagerProduct);
        productsAdapter = new ProductsAdapter(products);
        rvProduct.setAdapter(productsAdapter);
        productsAdapter.setOnItemProductClickListener(this);
    }

    @Override
    public void onItemCategoryClick(int position) {
        CategoryProduct category = categories.get(position);
        Intent intent = new Intent(getActivity(), CategoryDetail.class);
        intent.putExtra("category", (Serializable) category);
        startActivity(intent);
    }

    @Override
    public void onItemProductClick(int position) {
        Product product = products.get(position);
        Intent intent = new Intent(getActivity(), ProductDetail.class);
        intent.putExtra("product", (Serializable) product);
        startActivity(intent);
    }
}
