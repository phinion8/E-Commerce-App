package com.phinion.e_commerceapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.content.Intent;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.phinion.e_commerceapplication.databinding.ActivityMainBinding;

import org.imaginativeworld.whynotimagecarousel.model.CarouselItem;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    ArrayList<Category> categories;
    ArrayList<Product> products;
    CategoryAdapter categoryAdapter;
    ProductAdapter productAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.searchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {

            }

            @Override
            public void onSearchConfirmed(CharSequence text) {

                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                intent.putExtra("query", text.toString());
                startActivity(intent);

            }

            @Override
            public void onButtonClicked(int buttonCode) {

            }
        });

        initCategories();
        initProducts();
        initSlider();







    }

    private void initSlider() {

        getRecentOffers();
    }

    private void initProducts() {

        products = new ArrayList<>();

        productAdapter = new ProductAdapter(this, products);

        GridLayoutManager productLayoutManager = new GridLayoutManager(this, 2);

        getProducts();

        binding.productList.setLayoutManager(productLayoutManager);
        binding.productList.setAdapter(productAdapter);



    }
    private void initCategories() {

        categories = new ArrayList<>();

        categoryAdapter = new CategoryAdapter(this, categories);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 4);

        getCategories();

        binding.categoryList.setAdapter(categoryAdapter);
        binding.categoryList.setLayoutManager(layoutManager);



    }

    void getCategories(){
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.GET, Constants.GET_CATEGORIES_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    JSONObject mainObj = new JSONObject(response);
                    if (mainObj.getString("status").equals("success")){

                        JSONArray categoriesArray = mainObj.getJSONArray("categories");
                        for (int i = 0; i < categoriesArray.length(); i++){
                            JSONObject object = categoriesArray.getJSONObject(i);
                            Category category = new Category(
                                    object.getString("name"),
                                    Constants.CATEGORIES_IMAGE_URL + object.getString("icon"),
                                    object.getString("color"),
                                    object.getString("brief"),
                                    object.getInt("id")
                            );
                            categories.add(category);
                        }

                        categoryAdapter.notifyDataSetChanged();
                    }else {
                        //Do nothing
                    }

                }catch (JSONException e){
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


    void getProducts(){
        RequestQueue queue = Volley.newRequestQueue(this);

        String url = Constants.GET_PRODUCTS_URL + "?count=8";

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

    void getRecentOffers(){
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest request = new StringRequest(Request.Method.GET, Constants.GET_OFFERS_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                try {
                    JSONObject mainObj = new JSONObject(response);

                    if (mainObj.getString("status").equals("success")){
                        JSONArray offersJsonArray = mainObj.getJSONArray("news_infos");
                        for (int i = 0; i < offersJsonArray.length(); i++){
                            JSONObject object = offersJsonArray.getJSONObject(i);
                            binding.carousel.addData(new CarouselItem(Constants.NEWS_IMAGE_URL + object.getString("image"), object.getString("title")));
                        }
                    }

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



}