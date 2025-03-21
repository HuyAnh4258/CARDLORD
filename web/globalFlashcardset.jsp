<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="java.util.List" %>
<%@ page import="model.flashcardsetDAO" %>
<%@ page import="model.FlashcardSet" %>
<%@ page import="java.util.Set" %>
<%@ page import="java.util.HashSet" %>
<%@ page import="java.net.URLDecoder" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String publicSort = request.getParameter("publicSort");
    flashcardsetDAO dao = new flashcardsetDAO();
    List<FlashcardSet> list = null;
    if (publicSort != null && !publicSort.isEmpty()) {
        list = dao.getallPublic(publicSort);
    } else {
        list = dao.getallPublic();
    }
    
    // Get user's ID from session
    Integer userId = (Integer) session.getAttribute("user_id");
    
    // Get favorited flashcard sets from cookie
    Set<String> favoritedSets = new HashSet<>();
    if (userId != null) {
        String cookieName = "favouriteFlashcards_" + userId;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookieName.equals(cookie.getName())) {
                    String favouriteList = URLDecoder.decode(cookie.getValue(), "UTF-8");
                    if (!favouriteList.isEmpty()) {
                        String[] items = favouriteList.split(",");
                        for (String item : items) {
                            favoritedSets.add(item);
                        }
                    }
                    break;
                }
            }
        }
    }
    
    // Phân trang
    int itemsPerPage = 8; // Số flashcard trên mỗi trang
    int currentPage = 1;
    String pageParam = request.getParameter("page");
    if (pageParam != null && !pageParam.isEmpty()) {
        try {
            currentPage = Integer.parseInt(pageParam);
            if (currentPage < 1) currentPage = 1;
        } catch (NumberFormatException e) {
            currentPage = 1;
        }
    }
    
    int totalItems = list.size();
    int totalPages = (int) Math.ceil((double) totalItems / itemsPerPage);
    
    // Đảm bảo trang hiện tại không vượt quá tổng số trang
    if (currentPage > totalPages && totalPages > 0) {
        currentPage = totalPages;
    }
    
    // Tính chỉ số bắt đầu và kết thúc
    int startIndex = (currentPage - 1) * itemsPerPage;
    int endIndex = Math.min(startIndex + itemsPerPage, totalItems);
    
    // Lấy danh sách flashcard cho trang hiện tại
    List<FlashcardSet> currentPageItems = list;
    if (startIndex < totalItems) {
        currentPageItems = list.subList(startIndex, endIndex);
    } else {
        currentPageItems = list.subList(0, 0); // Trả về danh sách trống nếu trang không hợp lệ
    }
%>
<!DOCTYPE html>
<html>
    <head>
        
        <title>Public FlashCard Sets</title>
        <style>
            body {
                font-family: Arial, sans-serif;
                background-color: #f8f9fa;
                text-align: center;
                padding: 20px;
            }

            .container {
                display: flex;
                flex-wrap: wrap;
                justify-content: center;
                gap: 20px;
                margin-top: 20px;
            }

            .flashcard-box {
                width: 250px;
                background: white;
                border-radius: 15px;
                padding: 15px;
                box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
                text-align: left;
                transition: 0.3s;
            }

            .flashcard-box:hover {
                transform: scale(1.1);
            }

            .flashcard-box h3 {
                margin: 0;
                font-size: 18px;
                color: #333;
                font-weight: bold;
                white-space: nowrap;
                overflow: hidden;
                text-overflow: ellipsis;
            }

            .flashcard-box p {
                margin: 5px 0;
                font-size: 14px;
                color: #666;
                overflow: hidden;
                white-space: nowrap;
                text-overflow: ellipsis;
                max-width: 100%;
            }

            .actions {
                margin-top: 10px;
                display: flex;
                justify-content: center;
            }

            .actions a {
                text-decoration: none;
                color: white;
                background: #007bff;
                padding: 5px 10px;
                border-radius: 5px;
                font-size: 12px;
                transition: all 0.3s ease;
            }

            .actions a:hover {
                opacity: 0.8;
                cursor: pointer;
            }

            .fav {
                color: white;
                background-color: #f39c12 !important;
                cursor: pointer;
            }

            .favorited {
                background-color: #c9780f !important;
                
            }
            
            /* Pagination styles */
            .pagination {
                display: flex;
                justify-content: center;
                margin-top: 30px;
                list-style-type: none;
                padding: 0;
            }
            
            .pagination li {
                margin: 0 5px;
            }
            
            .pagination a {
                display: inline-block;
                padding: 8px 12px;
                background-color: #007bff;
                color: white;
                text-decoration: none;
                border-radius: 5px;
                transition: background-color 0.3s;
            }
            
            .pagination a:hover {
                background-color: #0056b3;
            }
            
            .pagination .active a {
                background-color: #0056b3;
                font-weight: bold;
            }
            
            .pagination .disabled a {
                background-color: #cccccc;
                cursor: not-allowed;
            }
        </style>
    </head>
    <body>
        <!-- Form sắp xếp -->
        <form style="margin-bottom: 50px; justify-content: center; width: 100%; background-color: #545454; padding: 20px;" action="dashboard.jsp" method="GET" class="d-flex align-items-center mb-3">
            <label style="font-weight: bolder; color: white" for="publicSort" class="me-2">Sort by:</label>
            <select id="publicSort" name="publicSort" class="form-select w-auto">
                <option value="" disabled selected style="color: #b8b8b8">Sort option</option>
                <option value="date_desc" <%= "date_desc".equals(publicSort) ? "selected" : "" %>>Date (Newest)</option>
                <option value="date_asc" <%= "date_asc".equals(publicSort) ? "selected" : "" %>>Date (Oldest)</option>
                <option value="view_desc" <%= "view_desc".equals(publicSort) ? "selected" : "" %>>Views (High to Low)</option>
                <option value="view_asc" <%= "view_asc".equals(publicSort) ? "selected" : "" %>>Views (Low to High)</option>
            </select>
            <button type="submit" class="btn btn-primary ms-2">Sort</button>
            <% if (publicSort != null && !publicSort.isEmpty()) { %>
                <input type="hidden" name="publicSort" value="<%= publicSort %>">
            <% } %>
        </form>
        <div class="container">
            <%
                if (currentPageItems != null && !currentPageItems.isEmpty()) {
                    for (FlashcardSet c : currentPageItems) {
                        // Check if this flashcard set is in user's favorites
                        boolean isFavorited = favoritedSets.contains(String.valueOf(c.getFlashcard_set_id()));
            %>
            <div class="flashcard-box">
                <h3><%= c.getName_of_flashcardset() %></h3>
                <p><strong>Tag:</strong> <%= c.getTags() %></p>
                <p><strong>Time created:</strong> <%= c.getTime_created() %></p>
                <p><strong>Views:</strong> <%= c.getView() %></p>
                <div class="actions" style="justify-content: left; gap: 5px">
                    <a href="updateView?id=<%= c.getFlashcard_set_id() %>">Learn</a>
                    <a href="practise.jsp?id=<%= c.getFlashcard_set_id() %>" style="background-color: red">Practice</a>
                    <a class="fav <%= isFavorited ? "favorited" : "" %>" 
                       id="fav-<%= c.getFlashcard_set_id() %>"
                       onclick="toggleFavorite('<%= c.getFlashcard_set_id() %>')" 
                       style="background-color: <%= isFavorited ? "#e74c3c" : "#f39c12" %>">
                        <%= isFavorited ? "Favorited" : "Favorite" %>
                    </a>
                </div>
            </div>
            <%
                    }
                } else {
            %>
            <tr>
                <td colspan="6">No flashcard sets found.</td>
            </tr>
            <%
                }
            %>
        </div>
        
        <!-- Phân trang -->
        <% if (totalPages > 1) { %>
        <ul class="pagination">
            <!-- Nút Previous -->
            <li class="<%= currentPage == 1 ? "disabled" : "" %>">
                <a href="<%= currentPage > 1 ? "dashboard.jsp?page=" + (currentPage - 1) + (publicSort != null && !publicSort.isEmpty() ? "&publicSort=" + publicSort : "") : "#" %>">
                    &laquo; Previous
                </a>
            </li>
            
            <% 
            // Hiển thị tối đa 5 trang
            int startPage = Math.max(1, currentPage - 2);
            int endPage = Math.min(startPage + 4, totalPages);
            
            // Điều chỉnh nếu không đủ 5 trang
            if (endPage - startPage < 4) {
                startPage = Math.max(1, endPage - 4);
            }
            
            for (int i = startPage; i <= endPage; i++) { 
            %>
                <li class="<%= i == currentPage ? "active" : "" %>">
                    <a href="dashboard.jsp?page=<%= i %><%= publicSort != null && !publicSort.isEmpty() ? "&publicSort=" + publicSort : "" %>">
                        <%= i %>
                    </a>
                </li>
            <% } %>
            
            <!-- Nút Next -->
            <li class="<%= currentPage == totalPages ? "disabled" : "" %>">
                <a href="<%= currentPage < totalPages ? "dashboard.jsp?page=" + (currentPage + 1) + (publicSort != null && !publicSort.isEmpty() ? "&publicSort=" + publicSort : "") : "#" %>">
                    Next &raquo;
                </a>
            </li>
        </ul>
        <% } %>
        
        <script>
            function toggleFavorite(flashcardSetId) {
                const favButton = document.getElementById('fav-' + flashcardSetId);
                const isFavorited = favButton.classList.contains('favorited');

                if (isFavorited) {
                    // If already favorited, call unfavorite endpoint
                    fetch('unfavourite', {
                        method: 'POST',
                        headers: {
                            'Content-Type': 'application/x-www-form-urlencoded',
                        },
                        body: 'flashcardId=' + flashcardSetId
                    })
                            .then(response => response.text())
                            .then(data => {
                                // Toggle the class and style
                                favButton.classList.remove('favorited');
                                favButton.style.backgroundColor = '#f39c12';
                                favButton.textContent = 'Favorite';
//                                alert("Removed from favorites");
                            })
                            .catch(error => console.error('Error:', error));
                } else {
                    // If not favorited, call favorite endpoint
                    fetch('favourite?id=' + flashcardSetId)
                            .then(response => response.text())
                            .then(data => {
                                // Toggle the class and style
                                favButton.classList.add('favorited');
                                favButton.style.backgroundColor = '#c9780f';
                                favButton.textContent = 'Favorited';
//                                alert(data);
                            })
                            .catch(error => console.error('Error:', error));
                }
            }
        </script>
    </body>
</html>