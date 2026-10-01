# Book Store — 24110202

Ứng dụng cửa hàng sách xây dựng cho môn Lập trình Web, mã đề 01.

**Sinh viên:** Lương Viết Vĩ Đông  
**MSSV:** 24110202

## Tổng quan sản phẩm

Book Store hỗ trợ khách hàng tìm và đặt mua sách, đồng thời cung cấp trang quản trị để quản lý dữ liệu cửa hàng và theo dõi đơn. Giao diện được xây dựng bằng JSP, các thao tác dùng Servlet và dữ liệu được lưu trong SQL Server qua JPA/Hibernate.

## Chức năng đã thực hiện

### Khách hàng

- Xem danh sách sách có phân trang, thông tin chi tiết, tồn kho và đánh giá.
- Đăng ký tài khoản, xác thực email bằng OTP; OTP hết hạn sau 5 phút và giới hạn 5 lần nhập sai.
- Đăng nhập và đăng xuất.
- Thêm sách vào giỏ, cập nhật số lượng hoặc xóa sách. Giỏ được lưu theo phiên; số lượng không vượt tồn kho hiện tại.
- Đặt hàng bằng COD với tên người nhận, số điện thoại, địa chỉ và ghi chú.
- Xem lịch sử đơn, lọc theo trạng thái và phân trang.
- Gửi đánh giá từ 1 đến 5 sao; mỗi tài khoản chỉ đánh giá một lần cho mỗi sách.

### Quản trị

- Thêm, sửa, xóa sách; quản lý số lượng và thông tin tác giả.
- Tải ảnh bìa JPEG, PNG, GIF hoặc WebP, tối đa 5 MB.
- Xem và lọc danh sách đơn hàng; cập nhật trạng thái theo vòng đời hợp lệ.
- Các trang quản trị chỉ dành cho tài khoản admin.

## Luồng đặt hàng và trạng thái

Khi checkout, hệ thống kiểm tra lại sách, giá và tồn kho trong database. Tồn kho được trừ cùng transaction với việc tạo đơn; nếu một dòng hàng không đủ tồn, toàn bộ thao tác được rollback. Đơn lưu tên sách và đơn giá tại thời điểm mua để lịch sử vẫn chính xác nếu catalog thay đổi.

| Mã trạng thái | Hiển thị |
|---|---|
| `NEW` | Đơn hàng mới |
| `CONFIRMED` | Đã xác nhận |
| `PREPARING` | Chuẩn bị hàng |
| `SHIPPING` | Vận chuyển |
| `OUT_FOR_DELIVERY` | Giao hàng |
| `DELIVERED` | Đã giao |
| `CANCELLED` | Đơn hàng hủy |
| `RETURNED` | Đơn hàng hoàn |

Admin chuyển đơn theo thứ tự xử lý; có thể hủy trước khi vận chuyển và chỉ chuyển sang hoàn sau khi đã giao. Để thử bộ lọc lịch sử, có thể sửa status trực tiếp trong SSMS bằng một trong các mã trên. Cách này chỉ đổi trạng thái hiển thị, không tự hoàn tồn kho.

## Điểm nổi bật phía backend

### Validation cụ thể

Validation được đặt ở service cho dữ liệu nghiệp vụ và ở controller/filter cho tham số request, quyền truy cập. Một số rule hiện có:

| Đầu vào | Kiểm tra backend |
|---|---|
| Đăng ký | Email đúng định dạng và chưa tồn tại; họ tên bắt buộc, tối đa 50 ký tự; mật khẩu từ 6 đến 32 ký tự; mật khẩu xác nhận phải trùng. OTP 6 chữ số, có thời hạn 5 phút và tối đa 5 lần nhập sai. |
| Giỏ hàng | `bookId`/`quantity` phải parse thành số nguyên; ID sách dương và sách phải tồn tại; thêm/cập nhật số lượng phải từ 1 đến tồn kho. Thêm trùng kiểm tra phần tồn còn lại trước khi cộng để tránh vượt giới hạn số nguyên/tồn kho. |
| Checkout COD | Trim trước khi kiểm tra: `recipientName` bắt buộc, ≤100 ký tự; `phone` khớp `0\d{9}`; `shippingAddress` bắt buộc, ≤255 ký tự; `note` tùy chọn, ≤500 ký tự. Form sai trả lỗi đúng field và vẫn giữ nguyên dữ liệu nhập. |
| Review | `bookId` và điểm phải parse hợp lệ; điểm từ 1–5; nội dung sau trim không được trống, tối đa 2000 ký tự; kiểm tra sách tồn tại và tài khoản chưa đánh giá sách đó. |
| Sách/tác giả | Kiểm tra giới hạn theo cột DB, số và ngày đúng định dạng; ID tác giả phải hợp lệ và tồn tại. Lỗi render lại form theo field, ID tài nguyên thiếu/sai trả 404. |
| Ảnh bìa | Tối đa 5 MB; MIME phải thuộc JPEG/PNG/GIF/WebP và chữ ký bytes phải khớp định dạng. |
| Phân trang/lọc đơn | `page` sai hoặc nhỏ hơn 1 thành trang 1, trang vượt tổng được clamp về trang cuối; status không khớp enum được xử lý như `ALL`. |
| Chuyển trạng thái | ID đơn phải dương, mã đích phải parse thành trạng thái hợp lệ; service kiểm tra transition từ trạng thái hiện tại trước khi repository ghi. |

### Quy ước response HTML cho giỏ và đơn hàng

Các luồng cart/checkout/order dùng cùng quy ước HTML (không tạo JSON API):

| Tình huống | Kết quả trả về |
|---|---|
| Validation form thất bại | Render lại JSP; đặt `form`, `errors` theo tên field và `message` vào request; giữ dữ liệu đã nhập. Lỗi tồn kho theo dòng được hiển thị và chặn checkout. |
| POST thành công | Redirect (PRG) về trang phù hợp và đặt `flashSuccess` trong session; flash được filter đưa sang request cho JSP hiển thị. Checkout thành công redirect `/orders` và xóa giỏ. |
| Xung đột nghiệp vụ sau POST | Redirect về trang danh sách với `flashError`, hoặc render checkout với lỗi global nếu người dùng cần sửa giỏ/form. |
| Chưa đăng nhập / không đủ quyền | Checkout và lịch sử redirect `/login`; URL checkout được lưu để tiếp tục sau login. User thường truy cập `/admin/*` nhận HTTP 403. |
| Không tìm thấy tài nguyên | ID sách sai/không tồn tại trả HTTP 404; đơn không tồn tại được báo lỗi nghiệp vụ ở thao tác cập nhật. |

Quy ước form HTML được áp dụng cho các luồng đăng nhập/đăng ký, catalog, giỏ hàng, checkout, đánh giá và quản trị. Lỗi nhập liệu render lại form với `errors` theo field; POST hợp lệ redirect kèm flash; ID tài nguyên không tồn tại trả HTTP 404.

### Nhất quán dữ liệu và an toàn nghiệp vụ

- Tạo đơn, tạo các dòng hàng và trừ tồn kho nằm trong cùng transaction. Repository khóa sách khi đọc, kiểm tra lại số lượng và dùng cập nhật có điều kiện `quantity >= số lượng đặt`; nếu bất kỳ dòng nào thất bại, transaction rollback toàn bộ.
- Giá, tên sách, tổng tiền, phương thức `COD` và trạng thái ban đầu `NEW` do backend xác định. `order_items` lưu snapshot tên và đơn giá, giúp lịch sử không đổi khi thông tin catalog được cập nhật sau này.
- Cập nhật trạng thái đơn cũng khóa bản ghi và kiểm tra transition trước khi ghi; ràng buộc database giới hạn các mã status hợp lệ.
- Truy vấn lịch sử user luôn kèm `userId` lấy từ session; người dùng không thể chọn userId tùy ý để đọc đơn tài khoản khác. Dữ liệu filter được bind parameter trong truy vấn JPA.
- Checkout dùng `FormResult_24110202<T>` cho lỗi field/global; danh sách đơn được lọc và phân trang ở repository, chỉ tải items cho trang đang xem thay vì tải toàn bộ lịch sử.

## Xử lý lỗi HTTP và logging

- `web.xml` định tuyến HTTP 403, 404, 500 và exception chưa xử lý tới `/error`.
- `ErrorController_24110202` giữ mã HTTP, đặt thông báo thân thiện rồi forward tới `/WEB-INF/views/error.jsp`. Trang không hiển thị chi tiết exception.
- Exception chưa xử lý được ghi bằng `java.util.logging` (JUL) ở mức `SEVERE`, kèm stack trace và URI request; lỗi HTTP thông thường như 403/404 không bị log như lỗi server.
- Luồng: Servlet/filter gọi `sendError(...)` hoặc ném exception → container áp dụng error-page mapping → `/error` đọc thuộc tính lỗi chuẩn Servlet → log exception nếu có → forward JSP với status ban đầu.

| HTTP status | Nội dung |
|---|---|
| 403 | Không có quyền truy cập |
| 404 | Không tìm thấy trang hoặc tài nguyên |
| 500 | Sự cố máy chủ; chi tiết chỉ có trong log server |

## Kiểm tra và test

Chạy toàn bộ kiểm thử:

```powershell
mvn clean test
```

Các test kiểm tra validation dữ liệu catalog, chữ ký file ảnh, response form, checkout, trạng thái đơn, cart và phân quyền. Lần chạy hiện tại có **18 test đạt, 0 lỗi**. Build WAR bằng `mvn clean package`.

## Kiến trúc và dữ liệu

- **Presentation:** Jakarta Servlet, JSP/JSTL và bộ lọc đăng nhập, phân quyền, thông báo.
- **Business:** Service kiểm tra dữ liệu và điều phối nghiệp vụ.
- **Data access:** Repository/JPA Entity với Hibernate.
- **Database:** SQL Server; các bảng chính gồm `users`, `books`, `author`, `book_author`, `rating`, `orders` và `order_items`.
- Form dùng validation và hiển thị lỗi theo trường; POST thành công dùng redirect và flash message.

## Tài khoản demo và đường dẫn

| Vai trò | Email | Mật khẩu |
|---|---|---|
| Admin | `admin@example.com` | `Admin@123` |
| Khách hàng | `user@example.com` | `User@123` |

| Chức năng | Đường dẫn |
|---|---|
| Trang chủ / danh sách sách | `/home` |
| Chi tiết sách | `/book/detail?id=1` |
| Giỏ hàng | `/cart` |
| Thanh toán COD | `/checkout` |
| Lịch sử đơn hàng | `/orders` |
| Quản lý sách | `/admin/books` |
| Quản lý tác giả | `/admin/authors` |
| Quản lý đơn hàng | `/admin/orders` |

## Hướng dẫn chạy

**Yêu cầu:** JDK 21, Maven 3.9+, SQL Server và Apache Tomcat 11.

1. Mở `database/bookstore_24110202.sql` trong SSMS và chạy script để tạo database, bảng và dữ liệu mẫu. Script không xóa database hoặc dữ liệu hiện có.
2. Cấu hình thông tin kết nối trước khi khởi động Tomcat:

   ```powershell
   $env:DB_URL="jdbc:sqlserver://localhost:1433;databaseName=BookStore_24110202;encrypt=true;trustServerCertificate=true"
   $env:DB_USER="sa"
   $env:DB_PASSWORD="your-sql-password"
   ```

   Nếu cần gửi OTP qua Gmail, cấu hình thêm `MAIL_USERNAME` và `MAIL_APP_PASSWORD` bằng Google App Password. Khi chạy Tomcat dưới dạng Windows service, cần đặt biến môi trường cho service/máy rồi khởi động lại service; biến đặt trong terminal không tự truyền vào service đang chạy.

3. Tạo WAR:

   ```powershell
   mvn clean package
   ```

4. Chép `target/bookstore-24110202.war` vào thư mục `webapps` của Tomcat 11 và khởi động Tomcat. Mở `http://localhost:8080/bookstore-24110202/`.

Không đưa mật khẩu SQL Server hoặc Gmail App Password vào bài nộp. Ảnh bìa tải lên được lưu trong `webapps/bookstore-24110202/uploads`; nếu cần nộp kèm ảnh, chép ảnh vào `src/main/webapp/uploads` trước khi build.
