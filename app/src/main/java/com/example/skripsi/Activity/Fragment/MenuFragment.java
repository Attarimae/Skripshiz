package com.example.skripsi.Activity.Fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.skripsi.API.ServiceGenerator;
import com.example.skripsi.API.SharedPreferencesCashier;
import com.example.skripsi.Adapter.MenuGridAdapter;
import com.example.skripsi.Model.CheckoutItemModel;
import com.example.skripsi.Model.Menus.MenuItemModel;
import com.example.skripsi.R;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MenuFragment extends Fragment {

    private ArrayList<MenuItemModel> menuList;
    private ArrayList<String> categoryList;
    private GridView menuGrid;
    private MenuGridAdapter adapter;
    private int inputPrice_Integer = 0;
    private int inputQty_Integer = 0;
    private boolean isInitialSetup = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu, container, false);

        menuGrid = view.findViewById(R.id.FR_gridMenu);
        menuList = new ArrayList<>();

        categoryList = new ArrayList<>();
        categoryList.add("All");

        SharedPreferencesCashier spc = new SharedPreferencesCashier(requireContext());

        adapter = new MenuGridAdapter(requireActivity(), menuList);
        menuGrid.setAdapter(adapter);

        ServiceGenerator service = new ServiceGenerator();
        Call<ArrayList<MenuItemModel>> call = service.getApiService(requireContext()).getAllMenu();
        call.enqueue(new Callback<ArrayList<MenuItemModel>>() {
            @Override
            public void onResponse(Call<ArrayList<MenuItemModel>> call, Response<ArrayList<MenuItemModel>> response) {
                if(response.isSuccessful()){
                    int menuListSize = response.body().size();
                    if(menuListSize == 0){
                        Toast.makeText(requireContext(), "There's no menu available right now...", Toast.LENGTH_SHORT).show();
                    } else {
                        for(int i=0; i < menuListSize; i++){
                            String menuCategory = response.body().get(i).getMenuCategory();
                            String menuName = response.body().get(i).getMenuName();
                            String menuPrice = "Rp. " + response.body().get(i).getMenuPrice();
                            String menuDescription = response.body().get(i).getMenuDescription();
                            int id = response.body().get(i).getId();
                            String menuImg = response.body().get(i).getImgID();
                            //menuList.add(new MenuItemModel(id, menuCategory, menuName, menuDescription, menuPrice, "R.drawable.ic_launcher_background"));
                            menuList.add(new MenuItemModel(id,menuCategory, menuName, menuDescription, menuPrice, menuImg));
                            MenuGridAdapter adapter = new MenuGridAdapter(requireActivity(), menuList);
                            menuGrid.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                            if(!categoryList.contains(menuCategory)){
                                categoryList.add(menuCategory);
                            }
                        }
                        Spinner categorySpinner = view.findViewById(R.id.FR_spinnerMenuCategory);

                        ArrayAdapter<String> categorySpinnerAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, categoryList);
                        categorySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        categorySpinner.setAdapter(categorySpinnerAdapter);

                        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                if (!isInitialSetup) {
                                    String selectedCategory = categoryList.get(position);
                                    filterMenuByCategory(selectedCategory);
                                } else {
                                    isInitialSetup = false;
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {
                                //Auto-Generated
                            }
                        });
                    }
                } else {
                    Toast.makeText(requireContext(), "Failed to connect the servers", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<MenuItemModel>> call, Throwable t) {
                Log.e("Fragment Menu", t.getMessage());
            }
        });

//        ExtendedFloatingActionButton menuFAB = view.findViewById(R.id.extend_menu_fab);
//        menuFAB.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //Display Form Isi Menu Manual (Nama, Harga, Quantity)
//                openAddMenuManualForm();
//            }
//        });

        //Search View
        SearchView menuSearchView = view.findViewById(R.id.FT_searchViewMenu);
        menuSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ArrayList<MenuItemModel> filteredMenuList = new ArrayList<>();
                for(MenuItemModel filter : menuList) {
                    if (filter.getMenuName().toLowerCase().contains(newText.toLowerCase())) {
                        filteredMenuList.add(filter);
                    }
                }

                MenuGridAdapter adapter = new MenuGridAdapter(requireActivity(), filteredMenuList);
                menuGrid.setAdapter(adapter);

                if(filteredMenuList.isEmpty()){
                    Toast.makeText(requireContext(), "Filter not found...", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });
        return view;
    }

    private void filterMenuByCategory(String selectedCategory){
        ArrayList<MenuItemModel> filteredMenuList = new ArrayList<>();
        if(selectedCategory.equals("All")){
            filteredMenuList = menuList;
        } else {
            for(MenuItemModel item : menuList){
                if(item.getMenuCategory().equals(selectedCategory)){
                    filteredMenuList.add(item);
                }
            }
        }
        MenuGridAdapter adapter = new MenuGridAdapter(requireActivity(), filteredMenuList);
        menuGrid.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void openAddMenuManualForm(){
        Dialog dialog = new Dialog(requireActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.form_add_menu_manual);
        dialog.setCancelable(true);
        dialog.show();

        EditText inputCategory = dialog.findViewById(R.id.form_add_menu_editTxtCategory);
        inputCategory.setInputType(InputType.TYPE_CLASS_TEXT);
        EditText inputName = dialog.findViewById(R.id.form_add_menu_editTxtName);
        inputName.setInputType(InputType.TYPE_CLASS_TEXT);
        EditText inputPrice = dialog.findViewById(R.id.form_add_menu_editTxtPrice);
        inputPrice.setInputType(InputType.TYPE_CLASS_NUMBER);
        EditText inputDesc = dialog.findViewById(R.id.form_add_menu_editTxtDesc);
        inputDesc.setInputType(InputType.TYPE_CLASS_TEXT);
        EditText inputQty = dialog.findViewById(R.id.form_add_menu_editTxtQty);
        inputQty.setInputType(InputType.TYPE_CLASS_NUMBER);

        Button buttonCompletePayment = dialog.findViewById(R.id.form_add_menu_okButton);
        buttonCompletePayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validationAddMenuManual(inputCategory, inputName, inputPrice, inputDesc, inputQty)){
                    dialog.dismiss();
                    finishAddMenuManual(inputCategory.getText().toString(), inputName.getText().toString(),
                            Integer.toString(inputPrice_Integer), inputDesc.getText().toString(), inputQty_Integer);
                }
            }
        });
    }

    private boolean validationAddMenuManual(EditText inputCategory, EditText inputName, EditText inputPrice, EditText inputDesc, EditText inputQty){
        if(inputCategory.getText().toString().isEmpty()){
            inputCategory.setError("Category can't be empty");
            return false;
        } else {
            inputCategory.setError(null);
        }

        if(inputName.getText().toString().isEmpty()){
            inputName.setError("Name can't be empty");
            return false;
        } else {
            inputName.setError(null);
        }

        if(inputDesc.getText().toString().isEmpty()){
            inputDesc.setError("Description can't be empty");
            return false;
        } else {
            inputDesc.setError(null);
        }

        String inputPrice_TXT = inputPrice.getText().toString();
        String inputQty_TXT = inputQty.getText().toString();

        if(!"".equals(inputPrice_TXT)){
            inputPrice_Integer = Integer.parseInt(inputPrice_TXT);
        }
        if(inputPrice_Integer == 0){
            inputPrice.setError("Price can't be 0");
            return false;
        } else {
            inputPrice.setError("null");
        }

        if(!"".equals(inputQty_TXT)){
            inputQty_Integer = Integer.parseInt(inputQty_TXT);
        }
        if(inputQty_Integer == 0){
            inputQty.setError("Quantity can't be 0");
            return false;
        } else {
            inputQty.setError("null");
        }
        return true;
    }

    private void finishAddMenuManual(String category, String name, String price, String description, int qty){
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("Point of Sales", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("checkoutList", null);
        Type type = new TypeToken<ArrayList<CheckoutItemModel>>() {}.getType();
        ArrayList<CheckoutItemModel> checkoutList = gson.fromJson(json, type);
        if(checkoutList == null){
            checkoutList = new ArrayList<>();
        }

        checkoutList.add(new CheckoutItemModel(name, ("Rp. " + price), category, description, "R.drawable.smallsalad", qty));

        SharedPreferences.Editor editor = sharedPreferences.edit();
        String checkoutListJson = gson.toJson(checkoutList);
        editor.putString("checkoutList", checkoutListJson);
        editor.apply();

        for(CheckoutItemModel item : checkoutList){
            System.out.println(item.getCheckoutMenuCategory() + "\n" + item.getCheckoutMenuName() +
                    "\n" + item.getCheckoutMenuPrice() + "\n" + item.getCheckoutMenuDescription() + item.getCheckoutMenuQuantity());
        }

        Toast.makeText(requireContext(), "Berhasil menambahkan " + name, Toast.LENGTH_LONG).show();
    }
}