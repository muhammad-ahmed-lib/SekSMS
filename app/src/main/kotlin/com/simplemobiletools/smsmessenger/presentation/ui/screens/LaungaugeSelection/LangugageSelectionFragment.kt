package com.simplemobiletools.smsmessenger.ui.LaungaugeSelection

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.simplemobiletools.smsmessenger.R
import com.simplemobiletools.smsmessenger.presentation.ui.componenets.AppText
import com.simplemobiletools.smsmessenger.presentation.ui.componenets.customBorder
import java.util.Locale

class LangugageSelectionFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {

                LanguageSelectionScreen(
                    context = requireActivity(),
                    gotoNextScreen = {
                        findNavController().navigate(R.id.action_languageSelection_to_permissionFragment)
                        (requireActivity() as? Activity)?.recreate()

                    }
                )
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}


@Composable
fun LanguageSelectionScreen(gotoNextScreen: () -> Unit, context: Context) {
    val languageList = listOf(
        Language("Arabic", "العربية", R.drawable.img_flag_uae),
        Language("Bengali", "বাংলা", R.drawable.imag_flag_bangal),
        Language("Englis" +
            "h", "English", R.drawable.english_fg),
        Language("French", "française", R.drawable.french_fg),
        Language("German", "Deutsch", R.drawable.german_fg),
        Language("Hindi", "हिंदी", R.drawable.hindi_fg),
        Language("Spanish", "Español", R.drawable.spanish_fg),
        Language("Urdu", "اردو", R.drawable.urdu_fg)
    )


    val languages = remember { languageList }
    var selectedIndex by remember { mutableStateOf(-1) }

    Scaffold(
        topBar = {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Transparent)
                    .statusBarsPadding()
                    .customBorder(
                        color = colorResource(id = R.color.border_color),
                        strokeWidth = 2.dp.value,
                        showBottom = true,
                    )

                    .padding(top = 5.dp, start = 15.dp, end = 15.dp, bottom = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                AppText(
                    "Select language",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.W600,
                    color = colorResource(R.color.charcoal_color)
                )

                Image(
                    painter = painterResource(R.drawable.ic_tick),
                    contentDescription = "tick",
                    modifier = Modifier
                        .size(24.dp)
                        .clickable {
                            gotoNextScreen.invoke()

                        },
                    contentScale = ContentScale.Fit
                )


            }


        }


    ) { paddingValues ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(paddingValues),
            contentPadding = PaddingValues(horizontal = 15.dp, vertical = 10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            itemsIndexed(languages) { index, language ->
                LanguageItem(
                    language = language,
                    isSelected = index == selectedIndex,
                    onClick = {
                        selectedIndex = index
                        setLocale(context, language.name)
                    }
                )
            }

        }


    }


}

data class Language(
    val name: String,
    val subname: String,
    val flagResId: Int
)


@Composable
fun LanguageItem(
    language: Language,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(5.dp))
            .fillMaxWidth()
            .background(colorResource(R.color.border_color).copy(0.2f))
            .clickable { onClick() }
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = language.flagResId),
            contentDescription = "flag",
            modifier = Modifier
                .size(28.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Fit

        )

        Spacer(modifier = Modifier.width(16.dp))

        Column {
            Text(
                text = language.name,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = language.subname,
                fontSize = 12.sp,
                color = Color.Gray
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        if (isSelected) {
            Image(
                painter = painterResource(R.drawable.ic_check_selection),
                contentDescription = "Selected",
                modifier = Modifier.size(20.dp),
                contentScale = ContentScale.Fit
            )
        }
    }
}


fun setLocale(context: Context, languageCode: String) {
    val locale = when (languageCode.lowercase()) {
        "arabic" -> Locale("ar")
        "bengali" -> Locale("bn")
        "english" -> Locale("en")
        "french" -> Locale("fr")
        "german" -> Locale("de")
        "hindi" -> Locale("hi")
        "spanish" -> Locale("es")
        else -> Locale.getDefault()
    }

    Locale.setDefault(locale)
    val config = context.resources.configuration
    config.setLocale(locale)
    context.resources.updateConfiguration(config, context.resources.displayMetrics)

}


