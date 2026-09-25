package vn.edu.vhu.ltdd.a1lifecycle;

import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    // TODO: thay 2201234567 bằng MSSV của bạn
    private static final String TAG = "A1_241A010130";
    private TextView tvLog;
    private final StringBuilder history = new StringBuilder();
    private int step = 0;

    /** Ghi một sự kiện ra Logcat và hiển thị lên màn hình bằng Html.fromHtml. */
    private void logEvent(String event) {
        step++;
        String time = new SimpleDateFormat("HH:mm:ss.SSS", Locale.getDefault())
                .format(new Date());
        String text = step + ". [" + time + "] " + event;
        Log.d(TAG, text);

        // Xác định mã màu HTML tùy thuộc vào sự kiện
        String colorHex;
        if (event.contains("onDestroy")) {
            colorHex = "#FF0000"; // Màu đỏ
        } else if (event.contains("onPause") || event.contains("onStop")) {
            colorHex = "#FF8C00"; // Màu cam (DarkOrange)
        } else if (event.contains("onCreate") || event.contains("onStart") || event.contains("onResume")) {
            colorHex = "#008000"; // Màu xanh lá
        } else {
            colorHex = "#808080"; // Màu xám cho các hàm khác
        }

        // Định dạng thẻ HTML font color và xuống dòng <br>
        String formattedLine = String.format("<font color='%s'>%s</font><br>", colorHex, text);
        history.append(formattedLine);

        if (tvLog != null) {
            // Sử dụng Html.fromHtml để parse mã màu HTML lên TextView
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                tvLog.setText(Html.fromHtml(history.toString(), Html.FROM_HTML_MODE_LEGACY));
            } else {
                tvLog.setText(Html.fromHtml(history.toString()));
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tvLog = findViewById(R.id.tvLog);
        Button btnClear = findViewById(R.id.btnClear);
        Button btnCrash = findViewById(R.id.btnCrash);
        Button btnFinish = findViewById(R.id.btnFinish);

        btnClear.setOnClickListener(v -> {
            history.setLength(0);
            step = 0;
            tvLog.setText("");
            Log.i(TAG, "---- Đã xóa lịch sử ----");
        });

        // Nút cố ý gây lỗi để luyện đọc stack trace trong Logcat
        btnCrash.setOnClickListener(v -> {
            try {
                String ten = null;
                Log.d(TAG, "Độ dài tên: " + ten.length());
            } catch (NullPointerException e) {
                Log.e(TAG, "Bắt được lỗi NullPointerException", e);
                Toast.makeText(this, "Đã bắt lỗi: tên đang null", Toast.LENGTH_SHORT).show();
            }
        });
        // finish() để quan sát onDestroy (Android 12+: Back không hủy Activity gốc)
        btnFinish.setOnClickListener(v -> finish());

        String state = (savedInstanceState == null) ? "= null" : "!= null";
        logEvent("onCreate (savedInstanceState " + state + ")");
    }

    @Override
    protected void onStart() {
        super.onStart();
        logEvent("onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        logEvent("onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        logEvent("onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        logEvent("onStop");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        logEvent("onRestart");
    }

    @Override
    protected void onDestroy() {
        logEvent("onDestroy");
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        logEvent("onSaveInstanceState");
    }
}