package com.example.money_stats

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.money_stats.helper.JsonHelper
import kotlinx.android.synthetic.main.currency_fragment.*
import java.io.InputStream
import java.net.URL
import org.json.JSONObject
import java.lang.Error
import kotlin.math.absoluteValue

class CurrencyFragment : DialogFragment() {

    companion object {
        fun newInstance() = CurrencyFragment()
    }

    private lateinit var viewModel: CurrencyViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.currency_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(CurrencyViewModel::class.java)

        val url = "https://api.exchangeratesapi.io/latest?base=BGN"

        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null,
            Response.Listener { response ->
                val currenciesResponse = JsonHelper.toMap(response)
                val currencies = mutableListOf<Currency>()
                (currenciesResponse["rates"] as HashMap<String, Double>).map{ (key, value) ->
                    val countryCode = key.dropLast(1)
                    val flagUrl = "https://www.countryflags.io/${countryCode}/flat/64.png"
                    currencies.add(Currency(value, key, flagUrl))
                }
                currencyItems.layoutManager = LinearLayoutManager(activity)
                currencyItems.adapter = CurrencyRecyclerAdapter(requireActivity(), currencies)
            },
            Response.ErrorListener { error ->
                // TODO: Handle error
            }
        )

        Volley.newRequestQueue(activity?.applicationContext).add(jsonObjectRequest)
    }
}