package ui.view.components

import androidx.compose.foundation.layout.size
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.nhom18_lttbdd_qldb_ngaybc.R

@Composable
fun BottomNavigationBar(navController: NavController, selected: String) {
    NavigationBar(
        containerColor = Color(0xB587FF95),
        tonalElevation = 4.dp

    ) {
        NavigationBarItem(
            selected = selected == "homedb",

            onClick = {
                if (selected != "homedb") navController.navigate("homedb")
            },
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_contact_navi),
                    contentDescription = "Danh bạ",
                    modifier = Modifier.size(30.dp)
                )
            },
            label = {
                Text(
                    text = "Danh bạ",
                    style = LocalTextStyle.current.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                )
            },
        )

        NavigationBarItem(
            selected = selected == "groupscreen",
            onClick = {
                if (selected != "groupscreen") navController.navigate("groupscreen")
            },
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_group_navi),
                    contentDescription = "Nhóm",
                    modifier = Modifier.size(30.dp)

                )
            },
            label = {
                Text(
                    text = "Nhóm",
                    style = LocalTextStyle.current.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                )
            },
        )

        NavigationBarItem(
            selected = selected == "setting",
            onClick = {
                if (selected != "setting") navController.navigate("setting")
            },
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_setting_navi),
                    contentDescription = "Cài đặt",
                    modifier = Modifier.size(30.dp)

                )
            },
            label = {
                Text(
                    text = "Cài đặt",
                    style = LocalTextStyle.current.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                )
            },

        )
    }
}