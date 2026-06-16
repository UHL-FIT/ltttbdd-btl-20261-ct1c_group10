package com.example.ungdungthoitiet.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ungdungthoitiet.data.TrangThaiUiThoiTiet
import androidx.compose.foundation.clickable
import androidx.compose.ui.tooling.preview.Preview
import com.example.ungdungthoitiet.ui.theme.UngDungThoiTietTheme

@Composable
fun BangCaiDat(
    trangThai: TrangThaiUiThoiTiet,
    onDoiNhietDo: (String) -> Unit,
    onDoiGio: (String) -> Unit,
    onDoiLuongMua: (String) -> Unit,
    onDoiKhoangCach: (String) -> Unit,
    onDong: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.4f))
            .clickable { onDong() }, // Bấm vào vùng trống/vùng đen mờ bên cạnh để đóng cài đặt
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .background(Color.White, shape = MaterialTheme.shapes.medium)
                .padding(16.dp)
                .clickable(enabled = false) { } // Khóa click tại đây, ngăn không cho tác động đến Trang chủ phía sau
        ) {
            Text("Bảng tùy chỉnh", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Spacer(Modifier.height(16.dp))

            ThanhChonDonVi("Nhiệt độ", "°C", "°F", trangThai.donViNhietDo, onDoiNhietDo)
            ThanhChonDonVi("Tốc độ gió", "km/h", "mph", trangThai.donViGio, onDoiGio)
            ThanhChonDonVi("Lượng mưa", "mm", "inch", trangThai.donViLuongMua, onDoiLuongMua)
            ThanhChonDonVi("Khoảng cách", "km", "mi", trangThai.donViKhoangCach, onDoiKhoangCach)

            Spacer(Modifier.height(16.dp))
            Button(onClick = onDong, modifier = Modifier.fillMaxWidth()) {
                Text("Đóng")
            }
        }
    }
}

@Composable
fun ThanhChonDonVi(
    tieuDe: String,
    chon1: String,
    chon2: String,
    giaTriHienTai: String,
    onThayDoi: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Text(tieuDe)
        Row(modifier = Modifier.selectableGroup(), verticalAlignment = Alignment.CenterVertically) {
            OptionRadio(chon1, giaTriHienTai == chon1) { onThayDoi(chon1) }
            Spacer(Modifier.width(32.dp))
            OptionRadio(chon2, giaTriHienTai == chon2) { onThayDoi(chon2) }
        }
    }
}

@Composable
fun OptionRadio(text: String, selected: Boolean, onClick: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.selectable(selected = selected, onClick = onClick, role = Role.RadioButton)
    ) {
        RadioButton(selected = selected, onClick = null)
        Text(text)
    }
}

@Preview(showBackground = true)
@Composable
fun XemTruocThanhChonDonVi() {
    UngDungThoiTietTheme {
        ThanhChonDonVi(
            tieuDe = "Nhiệt độ",
            chon1 = "°C",
            chon2 = "°F",
            giaTriHienTai = "°C",
            onThayDoi = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun XemTruocOptionRadio() {
    UngDungThoiTietTheme {
        Row(modifier = Modifier.padding(16.dp)) {
            OptionRadio(text = "Đang chọn", selected = true, onClick = {})
            Spacer(Modifier.width(16.dp))
            OptionRadio(text = "Chưa chọn", selected = false, onClick = {})
        }
    }
}

@Preview(showBackground = true)
@Composable
fun XemTruocBangCaiDat() {
    UngDungThoiTietTheme {
        BangCaiDat(
            trangThai = TrangThaiUiThoiTiet(
                donViNhietDo = "°C",
                donViGio = "km/h",
                donViLuongMua = "mm",
                donViKhoangCach = "km"
            ),
            onDoiNhietDo = {},
            onDoiGio = {},
            onDoiLuongMua = {},
            onDoiKhoangCach = {},
            onDong = {}
        )
    }
}
