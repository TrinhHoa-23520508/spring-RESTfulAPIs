package vn.hoidanit.jobhunter.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.hoidanit.jobhunter.domain.RestResponse;

@RestController
public class HelloWorldController {

    @GetMapping("/")
    public ResponseEntity<RestResponse<String>> getHelloWorld() {
        // Tạo đối tượng RestResponse
        RestResponse<String> response = new RestResponse<>();
        response.setStatusCode(200);
        response.setError(null);  // Không có lỗi
        response.setMessage("Hello World ");  // Đây là message dạng String
        response.setData(null);  // Không có dữ liệu thêm

        // Trả về RestResponse dưới dạng JSON với mã HTTP 200
        return ResponseEntity.ok(response);
    }
}
