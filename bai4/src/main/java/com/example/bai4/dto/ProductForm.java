    package com.example.bai4.dto;

    import jakarta.validation.constraints.*;
    import lombok.Getter;
    import lombok.Setter;

    @Getter @Setter
    public class ProductForm {

        private Long id;

        @NotBlank(message = "Tên sản phẩm không được để trống")
        private String name;

        @NotNull(message = "Giá sản phẩm không được để trống")
        @Min(value = 1, message = "Giá sản phẩm phải từ 1 đến 9999999")
        @Max(value = 9999999, message = "Giá sản phẩm phải từ 1 đến 9999999")
        private Integer price;

        @NotNull(message = "Vui lòng chọn danh mục")
        private Long categoryId;

        // chỉ validate độ dài tên ảnh (sau khi upload)
        @Size(max = 200, message = "Tên hình ảnh không quá 200 ký tự")
        private String imageName;
    }
