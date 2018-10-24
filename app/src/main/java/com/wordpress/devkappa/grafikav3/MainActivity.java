package com.wordpress.devkappa.grafikav3;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class MainActivity extends AppCompatActivity {

    private RecyclerView gpuRecycler;
    private FirestoreRecyclerAdapter<Gpu, GpuHolder> adapter;
    LinearLayoutManager linearLayoutManager;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        initSpinnerSortBy();
        initSpinnerSortOrder();
        initSpinnerFilterType();
        initSpinnerFilterPrice();
        webScrape();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_search) {
            return true;
        }
        if (id == R.id.action_compare) {
        }


        return super.onOptionsItemSelected(item);
    }


    private void init() {
        gpuRecycler = findViewById(R.id.recycler_main_gpulist);
        gpuRecycler.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        gpuRecycler.setLayoutManager(linearLayoutManager);
        Toolbar mToolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(mToolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("GPU Database");
        db = FirebaseFirestore.getInstance();

    }

    private void initSpinnerSortBy() {
        final Spinner spinnerSortBy = findViewById(R.id.spinner_mainactivity_sortby);
        final Spinner spinnerFilterType = findViewById(R.id.spinner_mainactivity_filtertype);
        final Spinner spinnerFilterPrice = findViewById(R.id.spinner_mainactivity_filterprice);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.sortby, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSortBy.setAdapter(adapter);
        spinnerSortBy.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String sortByRaw = parent.getItemAtPosition(position).toString().toLowerCase();
                if (sortByRaw.equals("bench")) {
                    getFromDatabase("bench",
                            "gpu" + spinnerFilterType.getSelectedItem().toString(),
                            spinnerFilterPrice.getSelectedItem().toString());
                }
                if (sortByRaw.equals("price")) {
                    getFromDatabase("price",
                            "gpu" + spinnerFilterType.getSelectedItem().toString(),
                            spinnerFilterPrice.getSelectedItem().toString());
                }
                if (sortByRaw.equals("value")) {
                    getFromDatabase("value", "gpu" + spinnerFilterType.getSelectedItem().toString(),
                            spinnerFilterPrice.getSelectedItem().toString());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void initSpinnerSortOrder() {
        Spinner spinnerSortOrder = findViewById(R.id.spinner_mainactivity_sortorder);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.sortorder, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSortOrder.setAdapter(adapter);
        spinnerSortOrder.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String sortOrderRaw = parent.getItemAtPosition(position).toString();

                if (sortOrderRaw.equals("Ascending")) {
                    linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, true);
                    linearLayoutManager.setStackFromEnd(true);
                    gpuRecycler.setLayoutManager(linearLayoutManager);

                }
                if (sortOrderRaw.equals("Descending")) {
                    linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                    gpuRecycler.setLayoutManager(linearLayoutManager);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }


    private void initSpinnerFilterType() {
        Spinner spinnerFilterType = findViewById(R.id.spinner_mainactivity_filtertype);
        final Spinner spinnerSortBy = findViewById(R.id.spinner_mainactivity_sortby);
        final Spinner spinnerFilterPrice = findViewById(R.id.spinner_mainactivity_filterprice);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.filtertype, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFilterType.setAdapter(adapter);
        spinnerFilterType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String filterTypeRaw = parent.getItemAtPosition(position).toString();
                if (filterTypeRaw.equals("All")) {
                    getFromDatabase(spinnerSortBy.getSelectedItem().toString().toLowerCase(),
                            "gpu" + filterTypeRaw,
                            spinnerFilterPrice.getSelectedItem().toString());
                }
                if (filterTypeRaw.equals("Desktop")) {
                    getFromDatabase(spinnerSortBy.getSelectedItem().toString().toLowerCase(),
                            "gpu" + filterTypeRaw,
                            spinnerFilterPrice.getSelectedItem().toString());
                }
                if (filterTypeRaw.equals("Mobile")) {
                    getFromDatabase(spinnerSortBy.getSelectedItem().toString().toLowerCase(),
                            "gpu" + filterTypeRaw,
                            spinnerFilterPrice.getSelectedItem().toString());
                }
                if (filterTypeRaw.equals("Workstation")) {
                    getFromDatabase(spinnerSortBy.getSelectedItem().toString().toLowerCase(),
                            "gpu" + filterTypeRaw,
                            spinnerFilterPrice.getSelectedItem().toString());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void initSpinnerFilterPrice() {
        Spinner spinnerFilterPrice = findViewById(R.id.spinner_mainactivity_filterprice);
        final Spinner spinnerSortBy = findViewById(R.id.spinner_mainactivity_sortby);
        final Spinner spinnerFilterType = findViewById(R.id.spinner_mainactivity_filtertype);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.filterprice, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFilterPrice.setAdapter(adapter);
        spinnerFilterPrice.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String filterPriceRaw = parent.getItemAtPosition(position).toString();

                if (filterPriceRaw.equals("Any Price")) {
                    getFromDatabase(spinnerSortBy.getSelectedItem().toString().toLowerCase(),
                            "gpu" + spinnerFilterType.getSelectedItem().toString(),
                            "Any Price");
                }
                if (filterPriceRaw.equals("0 to 300")) {
                    getFromDatabase(spinnerSortBy.getSelectedItem().toString().toLowerCase(),
                            "gpu" + spinnerFilterType.getSelectedItem().toString(),
                            "0 to 300");
                }
                if (filterPriceRaw.equals("300 to 600")) {
                    getFromDatabase(spinnerSortBy.getSelectedItem().toString().toLowerCase(),
                            "gpu" + spinnerFilterType.getSelectedItem().toString(),
                            "300 to 600");
                }
                if (filterPriceRaw.equals("600 upward")) {
                    getFromDatabase(spinnerSortBy.getSelectedItem().toString().toLowerCase(),
                            "gpu" + spinnerFilterType.getSelectedItem().toString(),
                            "600 upward");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    public void getFromDatabase(String sortBy, String collectionPath, String filterPrice) {
        if (filterPrice.equals("Any Price")) {
            Query query =
                    db.collection(collectionPath)
                            .orderBy(sortBy, Query.Direction.DESCENDING);
            setToRecycler(query);
        } else {
            Query query =
                    db.collection(collectionPath)
                            .whereEqualTo("priceRange", filterPrice)
                            .orderBy(sortBy, Query.Direction.DESCENDING);
            setToRecycler(query);
        }
    }


    public void setToRecycler(Query query) {
        FirestoreRecyclerOptions<Gpu> options = new FirestoreRecyclerOptions.Builder<Gpu>()
                .setQuery(query, Gpu.class)
                .build();
        adapter = new FirestoreRecyclerAdapter<Gpu, GpuHolder>(options) {

            @Override
            protected void onBindViewHolder(@NonNull GpuHolder holder, int position, @NonNull Gpu gpu) {
                holder.textViewModel.setText(gpu.getModel());
                holder.textViewPrice.setText(String.valueOf(gpu.getPrice()));
                holder.textViewBench.setText(String.valueOf(gpu.getBench()));
                holder.textViewValue.setText(String.valueOf(gpu.getValue()));
                holder.textViewType.setText(String.valueOf(gpu.getType()));
            }
            @NonNull
            @Override
            public GpuHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.gpulist_layout, parent, false);
                return new GpuHolder(view);
            }
        };
        adapter.notifyDataSetChanged();
        adapter.startListening();
        gpuRecycler.setAdapter(adapter);
    }


    public void addToDatabase(String model, double price, double value,
                              int bench, String type, String priceRange) {
        Map<String, Object> docData = new HashMap<>();
        docData.put("model", model);
        docData.put("bench", bench);
        docData.put("value", value);
        docData.put("price", price);
        docData.put("type", type);
        docData.put("priceRange", priceRange);

        db.collection("gpu" + type).document(model).set(docData);
        db.collection("gpuAll").document(model).set(docData);
    }


    public void webScrape() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Document doc = Jsoup.connect("https://www.videocardbenchmark.net/GPU_mega_page.html").get();
                    Elements gpus = doc.select("[id^=gpu]");
                    int counter = 0;

                    for (Element i : gpus) {
                        if (counter <= 150) {
                            Elements gpuModel = i.select("a:nth-child(2)");
                            Elements gpuBench = i.select("td:nth-child(3)");
                            Elements gpuPrice = i.select("td:nth-child(2) > a");
                            Elements gpuValue = i.select("td:nth-child(4)");
                            Elements gpuType = i.select("td:nth-child(9)");

                            String strModel = gpuModel.text().replace("/", "");
                            String strBench = gpuBench.text().replace("/", "");
                            String strPrice = gpuPrice.text().replaceAll("[/$*,]", "");
                            String strValue = gpuValue.text().replace("/", "");
                            String strType = gpuType.text().replace("/", "");

                            int intBench = Integer.parseInt(strBench);
                            double dblPrice;
                            double dblValue;

                            if ((!strPrice.equals("")) && (!strValue.equals(""))
                                    && (!strType.equals("")) && (!strType.equals("Unknown"))) {
                                dblPrice = Double.parseDouble(strPrice);
                                dblValue = Double.parseDouble(strValue);
                            } else {
                                continue;
                            }

                            if (dblPrice <= 300) {
                                addToDatabase(strModel, dblPrice, dblValue, intBench, strType, "0 to 300");
                                counter++;
                            }
                            if (dblPrice >= 300 && dblPrice <= 600) {
                                addToDatabase(strModel, dblPrice, dblValue, intBench, strType, "300 to 600");
                                counter++;
                            }
                            if (dblPrice >= 600) {
                                addToDatabase(strModel, dblPrice, dblValue, intBench, strType, "600 upward");
                                counter++;
                            }

                        }
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                    }
                });
            }
        }).start();
    }


    public class GpuHolder extends RecyclerView.ViewHolder {

        TextView textViewModel, textViewValue, textViewPrice, textViewBench, textViewType;

        private GpuHolder(View itemView) {
            super(itemView);

            textViewModel = itemView.findViewById(R.id.text_gpulistlayout_model);
            textViewValue = itemView.findViewById(R.id.text_gpulistlayout_value);
            textViewPrice = itemView.findViewById(R.id.text_gpulistlayout_price);
            textViewBench = itemView.findViewById(R.id.text_gpulistlayout_bench);
            textViewType = itemView.findViewById(R.id.text_gpulistlayout_type);

        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();

        if (adapter != null) {
            adapter.stopListening();
        }
    }


    public void compareGpu(MenuItem item) {
        Intent intent = new Intent(MainActivity.this, CompareActivity.class);
        startActivity(intent);
    }


}



