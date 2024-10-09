package dev.forsythe.gum

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.OnUserEarnedRewardListener
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import dev.forsythe.gum.ui.theme.GumTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            var btnText by remember { mutableStateOf("Loading ad") }
            var btnEnable by remember { mutableStateOf(false) }

            GumTheme {

                val context = LocalContext.current
                val coroutineScope = rememberCoroutineScope()

                Scaffold() {
                    Box(
                        modifier = Modifier
                            .padding(it)
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        var rewardedAd: RewardedAd? = null
                        fun loadRewardedAdd(context: Context) {
                            RewardedAd.load(
                                context,
                                "ca-app-pub-3940256099942544/5224354917",
                                AdRequest.Builder().build(),
                                object : RewardedAdLoadCallback() {
                                    override fun onAdFailedToLoad(loadError: LoadAdError) {
                                        super.onAdFailedToLoad(loadError)
                                        rewardedAd = null
                                    }

                                    override fun onAdLoaded(p0: RewardedAd) {
                                        super.onAdLoaded(p0)
                                        rewardedAd = p0
                                        btnText = "View Advert"
                                        btnEnable = true
                                    }
                                }
                            )
                        }

                        fun showRewardedAdd(context: Context, onDismissed: () -> Unit) {
                            rewardedAd?.let {
                                it.show(
                                    context as Activity,
                                    OnUserEarnedRewardListener {
                                        Toast.makeText(
                                            context,
                                            "Reward Achieved!",
                                            Toast.LENGTH_SHORT
                                        )
                                            .show()
                                        loadRewardedAdd(context)
                                        onDismissed()
                                        rewardedAd = null

                                        btnText = "Loading add"
                                        btnEnable = false
                                    }
                                )
                            }
                        }

                        loadRewardedAdd(context)
                        Button(
                            enabled = btnEnable,
                            onClick = {
                                coroutineScope.launch {
                                    showRewardedAdd(context, onDismissed = {
                                        Toast.makeText(
                                            context,
                                            "Rewarded ad shown!",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    })
                                }
                            }
                        ) {
                            Text(btnText)
                        }
                    }
                }
            }

        }
    }
}



