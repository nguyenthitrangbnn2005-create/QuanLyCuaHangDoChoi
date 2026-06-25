# README.txt

## 1. Giới thiệu đề tài

### Tên đề tài

Xây dựng ứng dụng quản lý cửa hàng đồ chơi trên nền tảng Android.

### Bài toán

Hiện nay các cửa hàng đồ chơi thường quản lý sản phẩm, khách hàng, đơn hàng và doanh thu bằng phương pháp thủ công hoặc sổ sách, gây khó khăn trong việc theo dõi dữ liệu, dễ xảy ra sai sót và mất nhiều thời gian.

Đề tài xây dựng một ứng dụng Android nhằm hỗ trợ quản lý cửa hàng đồ chơi một cách hiệu quả, giúp tự động hóa các nghiệp vụ quản lý và nâng cao hiệu quả kinh doanh.

### Mục tiêu

* Quản lý sản phẩm.
* Quản lý khách hàng.
* Quản lý đơn hàng.
* Quản lý giỏ hàng.
* Thống kê doanh thu.
* Hỗ trợ tìm kiếm sản phẩm.
* Xây dựng giao diện thân thiện và dễ sử dụng trên Android.

---

## 2. Dataset

### Nguồn dữ liệu

Dữ liệu được xây dựng thủ công và lưu trữ bằng SQLite trong ứng dụng Android.

### Link tải

Không sử dụng bộ dữ liệu công khai.
Dữ liệu được khởi tạo trực tiếp trong cơ sở dữ liệu SQLite.

### Mô tả các bảng dữ liệu

#### Bảng Tài Khoản (TaiKhoan)

| Cột      | Kiểu dữ liệu | Mô tả            |
| -------- | ------------ | ---------------- |
| id       | INTEGER      | Mã tài khoản     |
| username | TEXT         | Tên đăng nhập    |
| password | TEXT         | Mật khẩu         |
| role     | TEXT         | Quyền người dùng |

#### Bảng Khách Hàng (KhachHang)

| Cột    | Kiểu dữ liệu | Mô tả          |
| ------ | ------------ | -------------- |
| maKH   | INTEGER      | Mã khách hàng  |
| tenKH  | TEXT         | Tên khách hàng |
| sdt    | TEXT         | Số điện thoại  |
| diaChi | TEXT         | Địa chỉ        |

#### Bảng Sản Phẩm (SanPham)

| Cột     | Kiểu dữ liệu | Mô tả         |
| ------- | ------------ | ------------- |
| maSP    | INTEGER      | Mã sản phẩm   |
| tenSP   | TEXT         | Tên sản phẩm  |
| gia     | REAL         | Giá bán       |
| soLuong | INTEGER      | Số lượng tồn  |
| hinhAnh | TEXT         | Đường dẫn ảnh |

#### Bảng Đơn Hàng (DonHang)

| Cột       | Kiểu dữ liệu | Mô tả         |
| --------- | ------------ | ------------- |
| maDH      | INTEGER      | Mã đơn hàng   |
| maKH      | INTEGER      | Mã khách hàng |
| ngayDat   | TEXT         | Ngày đặt      |
| tongTien  | REAL         | Tổng tiền     |
| trangThai | TEXT         | Trạng thái    |

#### Bảng Chi Tiết Đơn Hàng (ChiTietDonHang)

| Cột     | Kiểu dữ liệu | Mô tả       |
| ------- | ------------ | ----------- |
| maCTDH  | INTEGER      | Mã chi tiết |
| maDH    | INTEGER      | Mã đơn hàng |
| maSP    | INTEGER      | Mã sản phẩm |
| soLuong | INTEGER      | Số lượng    |

---

## 3. Pipeline hệ thống

### Bước 1: Tiền xử lý dữ liệu

* Nhập dữ liệu người dùng.
* Kiểm tra dữ liệu hợp lệ.
* Kiểm tra dữ liệu trống.
* Kiểm tra tài khoản tồn tại.

### Bước 2: Xử lý nghiệp vụ

* Thêm sản phẩm.
* Sửa sản phẩm.
* Xóa sản phẩm.
* Quản lý đơn hàng.
* Quản lý khách hàng.

### Bước 3: Lưu dữ liệu

* Dữ liệu được lưu vào SQLite.

### Bước 4: Hiển thị kết quả

* Hiển thị danh sách sản phẩm.
* Hiển thị đơn hàng.
* Hiển thị thống kê doanh thu.

### Bước 5: Đánh giá hệ thống

* Kiểm thử chức năng.
* Kiểm thử giao diện.
* Kiểm thử dữ liệu.

---

## 4. Mô hình sử dụng

### Kiến trúc 3 lớp (Three Layer Architecture)

#### Presentation Layer (UI)

* Giao diện người dùng.
* XML Layout.
* Activity.

#### Business Layer (BUS)

* Xử lý nghiệp vụ.
* Kiểm tra dữ liệu đầu vào.
* Điều phối luồng xử lý.

#### Data Access Layer (DAL)

* Kết nối SQLite.
* CRUD dữ liệu.

### Lý do lựa chọn

* Dễ bảo trì.
* Dễ mở rộng.
* Tăng khả năng tái sử dụng mã nguồn.
* Hỗ trợ làm việc nhóm hiệu quả.

---

## 5. Kết quả đạt được

### Chức năng đã hoàn thành

#### Khách hàng

✓ Đăng ký tài khoản

✓ Đăng nhập

✓ Quên mật khẩu

✓ Xem thông tin cá nhân

✓ Xem sản phẩm

✓ Tìm kiếm sản phẩm

✓ Thêm vào giỏ hàng

✓ Thanh toán

✓ Xem lịch sử đơn hàng

✓ Nhận thông báo

#### Quản trị viên

✓ Quản lý sản phẩm

✓ Quản lý khách hàng

✓ Quản lý đơn hàng

✓ Gửi thông báo

✓ Thống kê doanh thu

### Kết quả kiểm thử

* Tỷ lệ chức năng hoạt động thành công: 100%
* Không phát hiện lỗi nghiêm trọng trong quá trình kiểm thử.
* Hệ thống hoạt động ổn định trên Android.

---

## 6. Hướng dẫn chạy chương trình

### 6.1 Cài môi trường

Yêu cầu:

* Android Studio Hedgehog hoặc mới hơn.
* JDK 17.
* Android SDK 34.
* Gradle 8.x.

### 6.2 Chạy dự án

Bước 1:
Clone hoặc tải source code.

Bước 2:
Mở Android Studio.

Bước 3:
Chọn:

File → Open Project

Bước 4:
Chờ Gradle Sync hoàn tất.

Bước 5:
Kết nối điện thoại Android hoặc mở Emulator.

Bước 6:
Nhấn Run ▶ để chạy ứng dụng.

---

## 7. Demo sử dụng

### Tài khoản Admin

Username: admin

Password: admin123

### Tài khoản Khách hàng

Username: user

Password: 123456

---

## 8. Cấu trúc thư mục dự án

Project/

├── activities/

│   ├── LoginActivity.java

│   ├── RegisterActivity.java

│   ├── MainActivity.java

│

├── adapters/

│   ├── ProductAdapter.java

│

├── database/

│   ├── DatabaseHelper.java

│

├── models/

│   ├── Product.java

│   ├── Customer.java

│   ├── Order.java

│

├── res/

│   ├── layout/

│   ├── drawable/

│   ├── values/

│

├── AndroidManifest.xml

└── build.gradle

---

## 9. Công nghệ sử dụng

* Android Studio
* Java
* XML
* SQLite
* Material Design

---

## 10. Tác giả

Họ và tên:
Nguyễn Thị Trang
Nguyễn Thị Lan Anh

Mã sinh viên:
10123326
10123023

Lớp:
12523T.1

Trường:
Đại học Sư phạm Kỹ thuật Hưng Yên

Năm thực hiện:
2026
