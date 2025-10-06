# catalog

**Catalog Service** là thành phần chịu trách nhiệm quản lý dữ liệu danh mục **sản phẩm và biến thể**, danh mục này được dùng chung xuyên suốt trong toàn hệ thống.

## usecase

| Usecase                        | Desc                                                                         | Invariant                                                                                                                 |
|:-------------------------------|:-----------------------------------------------------------------------------|:--------------------------------------------------------------------------------------------------------------------------|
| Tạo sản phẩm                   | Tạo mới một sản phẩm chuẩn với thông tin cơ bản (title, mô tả, thuộc tính…)  | Sản phẩm do admin của sàn tạo mới,sử dụng chung cho tất cả Merchant,không cho phép trùng trong toàn hệ thống              |
| Tạo biến thể sản phẩm(variant) |                                                                              | Mỗi sản phẩm có nhiều biến thể, được phân biệt bởi giá trị của các thuộc tính đặc trưng                                   |
| Thay đổi giá cơ bản của SP     | Admin thay đổi giá cơ bản của sản phẩm                                       |                                                                                                                           |
| Cập nhật sản phẩm/biến thể     | Mở form chỉnh sửa thông tin cơ bản (title, description,...), sau đó save     |                                                                                                                           |
| Publish sản phẩm/biến thể      | Admin publish sản phẩm/biến thể, sau đó merchant có thể chọn để kinh doanh   | Sản phẩm được publish có đầy đủ các thông tin: 1.Được gán Category, 2.Có title. Variant được publish khi có ít nhất 1 ảnh |
| Retire sản phẩm/biến thể       | Admin Retire sản phẩm/biến thể, sau đó merchant không thể chọn để kinh doanh |                                                                                                                           |

