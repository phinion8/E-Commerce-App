package com.phinion.e_commerceapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.phinion.e_commerceapplication.databinding.ActivitySearchBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {


    ActivitySearchBinding binding;

    ArrayList<Product> products;
    ProductAdapter productAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        products = new ArrayList<>();

        productAdapter = new ProductAdapter(this, products);

        GridLayoutManager productLayoutManager = new GridLayoutManager(this, 2);


        String query = getIntent().getStringExtra("query");
        getProducts(query);

        getSupportActionBar().setTitle(query);

        binding.productList.setLayoutManager(productLayoutManager);
        binding.productList.setAdapter(productAdapter);



        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }

    void getProducts(String query){
        RequestQueue queue = Volley.newRequestQueue(this);

        String url = Constants.GET_PRODUCTS_URL + "?q=" + query;

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject mainObj = new JSONObject(response);
                    if (mainObj.getString("status").equals("success")){

                        JSONArray productsArray = mainObj.getJSONArray("products");

                        for (int i = 0; i < productsArray.length(); i++){
                            JSONObject object = productsArray.getJSONObject(i);
                            Product product = new Product(

                                    object.getString("name"),
                                    Constants.PRODUCTS_IMAGE_URL +  object.getString("image"),
                                    object.getString("status"),
                                    object.getDouble("price"),
                                    object.getDouble("price_discount"),
                                    object.getInt("stock"),
                                    object.getInt("id")

                            );

                            products.add(product);
                        }

                    }else {
                        //Do nothing
                    }

                    productAdapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }




            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        queue.add(request);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

}