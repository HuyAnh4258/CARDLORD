<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="jakarta.servlet.http.Cookie" %>
<%@ page import="java.net.URLDecoder" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>
<%@ page import="model.flashcardsetDAO" %>
<%@ page import="model.FlashcardSet" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <title>Favourite Flashcards - CARDLORD</title>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
        <link href="https://fonts.googleapis.com/css?family=Open+Sans:300i,400" rel="stylesheet">
        <link href="https://fonts.googleapis.com/css2?family=Sigmar&display=swap" rel="stylesheet">
        <link href="https://fonts.googleapis.com/css2?family=Kooperativ&display=swap" rel="stylesheet">
        <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined" />
        <style>
            .logo {
                font-family: 'Sigmar';
                text-shadow:
                    -2px -2px 0 black,
                    2px -2px 0 black,
                    -2px  2px 0 black,
                    2px  2px 0 black;
            }

            .menu-icon {
                width: 2rem;
            }

            .home-icon,
            .folder-icon,
            .bell-icon,
            .robot-icon {
                width: 3rem;
                filter: invert(100%);
            }

            .logo-name {
                font-size: 0.8em;
                color: white;
            }

            .image-button {
                background: none;
                border: none;
                padding-left: 20px;
                padding-right: 20px;
                margin: 0px;
                cursor: pointer;
                border-radius: 50px;
            }

            .left-content {
                background-color: #545454;
                height: 100vh;
            }

            .lower-left-inner {
                display: flex;
                flex-direction: column;
                align-items: center;
                margin-top: 16em;
            }

            .offcanvas-start {
                width: 5rem;
                max-width: 20%;
                background-color: #ececec;
                color: black;
            }

            .offcanvas.show {
                width: 20%;
            }

            .offcanvas-item {
                display: flex;
                align-items: center;
                color: black;
                padding: 1rem;
                cursor: pointer;
                border-radius: 10px;
            }

            .offcanvas-item:hover {
                background-color: #d3d3d3; /* Light gray */
                transition: background-color 0.3s ease;
            }

            .image-button:hover{
                background-color: #d3d3d3; /* Light gray */
                transition: background-color 0.3s ease;
            }

            .offcanvas-item img {
                width: 2rem;
                margin-right: 1rem;
            }

            .offcanvas-item span {
                display: none;
                white-space: nowrap;
            }

            .offcanvas.show .offcanvas-item span {
                display: inline;
            }

            .search-box {
                align-self: center;
                width: 100%;
            }

            .offcanvas-body {

            }

            .offcanvas-header {
                padding: 10% 10% 0px 10%;
            }


            .offcanvas-header button{
                border-radius: 50px;
            }

            .offcanvas-header button:hover{
                background-color: #d3d3d3; /* Light gray */
                transition: background-color 0.3s ease;
            }

            .search-box input {
                width: 60%;
                border-width: 2px;
                border-style: solid;
                border-color: #545454;
                border-radius: 10px;
                padding: 0.5rem 1rem;
                box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
                outline: none;
                justify-self: right;
            }

            .right-top-content-container {
                display: flex;
                grid-template-columns: 80% 20%;
                padding: 10px;
                position: fixed;
                top: 0;
                left: 0;
                width: 100%;
                color: white;
                text-align: center;
                font-size: 20px;
                z-index: 1000;
                background-color: rgb(235,235,235);
                border-bottom: #b8b8b8 2px solid;
                border-radius: 10px;
                box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
            }

            .right-small-utility {
                display: flex;
                flex-direction: row;
                gap: 1em;
                justify-content: flex-end;
                align-items: center;
            }

            .add-new-flashcard {
                font-size: 2em;
                width: 40px;
                align-self: center;
                border-radius: 50px;
                display: flex;
                justify-content: center;
                cursor: pointer;
                color: black;
                margin-right: 2%;
                align-items: center;
            }

            .right-small-utility {
                margin-right: 2%;
            }

            .logo {
                width: 15vw;
                align-self: center;
                margin-left: 3%;
            }

            .logo a{
                text-decoration: none;
                color: white;
            }

            .profile img{
                border-radius: 50px;
            }

            .profile img:hover{
                background-color: #d3d3d3; /* Light gray */
                transition: background-color 0.3s ease;
            }

            .add-new-flashcard{
                width: 40px;
                height: 40px;
                border-radius: 50px;
            }

            .add-new-flashcard:hover{
                background-color: #d3d3d3; /* Light gray */
                transition: background-color 0.3s ease;
            }

            /* Favourite Flashcards Specific Styles */
            .page-content {
                margin-top: 90px;
                padding: 20px;
                background-color: #f8f9fa;
                min-height: 60vh;
                display: flex;
                flex-direction: column;
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
                justify-content: left;
                gap: 5px;
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

            .unfav {
                color: white;
            }

            .page-header {
                margin-bottom: 30px;
                text-align: center;
            }

            .back-link {
                display: inline-block;
                margin: 10px 0 20px;
                background-color: #007bff;
                color: white;
                padding: 8px 15px;
                border-radius: 5px;
                text-decoration: none;
                transition: background-color 0.3s;
                font-weight: bold;
            }

            .back-link:hover {
                background-color: #0056b3;
                text-decoration: none;
                color: white;
            }

            .empty-message {
                text-align: center;
                padding: 20px;
                color: #666;
                font-size: 18px;
            }

            .page-title {
                margin-top: 30px;
                color: #333;
                font-weight: bold;
                font-size: 28px;
            }
            
            /* Pagination Styles */
            .pagination-container {
                display: flex;
                justify-content: center;
                margin-top: auto;
                padding-top: 30px;
                padding-bottom: 20px;
                width: 100%;
            }
            
            .pagination {
                display: flex;
                list-style: none;
                padding: 0;
                margin: 0;
            }
            
            .pagination li {
                margin: 0 5px;
            }
            
            .pagination li a {
                display: block;
                padding: 8px 16px;
                text-decoration: none;
                background-color: #fff;
                color: #007bff;
                border-radius: 5px;
                border: 1px solid #007bff;
                transition: all 0.3s ease;
            }
            
            .pagination li a:hover {
                background-color: #007bff;
                color: white;
            }
            
            .pagination li.active a {
                background-color: #007bff;
                color: white;
                cursor: default;
            }
            
            .pagination li.disabled a {
                color: #6c757d;
                background-color: #f8f9fa;
                border-color: #dee2e6;
                cursor: not-allowed;
            }
            
            .flashcards-wrapper {
                flex: 1;
            }
        </style>
    </head>
    <body style="padding: 0px; min-height: 90vh">
        <div class="container-fluid">
            <div class="row vh-80">
                <!-- Offcanvas for Sliding Content -->
                <div class="offcanvas offcanvas-start" tabindex="-1" id="offcanvas-menu">
                    <div class="offcanvas-header" style="align-items: center">
                        <h3 class="offcanvas-description" style="font-weight: bold">MENU</h3>
                        <button type="button" class="btn-close" data-bs-dismiss="offcanvas" aria-label="Close"></button>
                    </div>
                    <div class="offcanvas-body">
                        <div class="offcanvas-item" onclick="window.location.href = 'dashboard.jsp';">
                            <img src="icon_image/home.png" alt="Home Page">
                            <span>Home Page</span>
                        </div>
                        <div class="offcanvas-item" onclick="window.location.href = 'library.jsp';">
                            <img src="icon_image/library.png" alt="Your Library">
                            <span>Your Library</span>
                        </div>

                        <div class="offcanvas-item" onclick="window.location.href = 'favourite.jsp';">
                            <img src="icon_image/favourite.png" alt="Favourite">
                            <span>Favourite</span>
                        </div>
                        <div class="offcanvas-item" onclick="window.location.href = '/PRJ_SE1938/StatisticsServlet';">
                            <img src="icon_image/statistic.png" alt="Statistic_General">
                            <span>General Statistic</span>
                        </div>
                        <div class="offcanvas-item" onclick="window.location.href = '/PRJ_SE1938/StatisticsUserServlet';">
                            <img src="icon_image/user_statistic.png" alt="Statistic_User">
                            <span>User Statistic</span>
                        </div>
                        <div class="offcanvas-item" onclick="window.location.href = 'chatBot.jsp';">
                            <img src="icon_image/bot.png" alt="Chat Bot">
                            <span>Chat Bot</span>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Right Content -->
            <div class="col flex-grow-1" style="padding: 0px;">
                <div class="right-top-content-container">
                    <!-- Menu Button -->
                    <button class="image-button" data-bs-toggle="offcanvas" data-bs-target="#offcanvas-menu">
                        <img src="icon_image/menu.png" class="menu-icon my-3" alt="Menu Icon">
                    </button>
                    <!-- Logo -->
                    <div class="logo">
                        <a href="dashboard.jsp"><h1>CARDLORD</h1></a>
                    </div>

                    <!-- Search Box -->
                    <form action="searchFCset" class="search-box">
                        <input type="text" name="searchStr" placeholder="&#128269; Find your flashcard set now..." style="color: #545454">
                    </form>

                    <!-- Add new flashcard set & profile -->
                    <div class="right-small-utility" style="align-items: center">
                        <div class="add-new-flashcard" onclick="window.location.href = 'createFlashcardset.jsp';">+

                        </div>
                        <div class="profile">
                            <img src="icon_image/user.png" class="dropdown-toggle" role="button"
                                 data-bs-toggle="dropdown"
                                 style="width: 50px; height: 50px; cursor: pointer;">
                            <ul class="dropdown-menu">
                                <li><jsp:include page="miniProfile.jsp"></jsp:include></li>
                                    <li><a class="dropdown-item" href="QR.jsp">Become CARDLORD</a></li>
                                    <li><a class="dropdown-item" href="setting.jsp">Settings</a></li>
                                    <li>
                                        <hr class="dropdown-divider">
                                    </li>
                                    <li><a class="dropdown-item" href="contact.html">Customer Support</a></li>
                                    <li><a class="dropdown-item" href="helper_center.html">Help</a></li>
                                    <li><a class="dropdown-item" href="LogoutServlet">Log out</a></li>
                                </ul>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Page Content -->
                <div class="page-content">
                <%
                    Integer userId = (Integer) session.getAttribute("user_id");
                    if (userId == null) {
                %>
                <div class="empty-message">
                    <p class="text-danger">You need to log in.</p> 
                    <a href="login.jsp" class="back-link">Go to Login</a>
                </div>
                <%
                        return;
                    }
                %>

                <div class="page-header">
                    <h2 class="page-title">Your Favourite Flashcards</h2>
                </div>

                <div class="flashcards-wrapper">
                    <div class="container">
                        <%
                            String cookieName = "favouriteFlashcards_" + userId;
                            Cookie[] cookies = request.getCookies();
                            String favouriteList = "";
                            if (cookies != null) {
                                for (Cookie cookie : cookies) {
                                    if (cookieName.equals(cookie.getName())) {
                                        favouriteList = URLDecoder.decode(cookie.getValue(), "UTF-8");
                                        break;
                                    }
                                }
                            }
                    
                            List<String> favourites = new ArrayList<>();
                            if (!favouriteList.isEmpty()) {
                                String[] items = favouriteList.split(",");
                                for (String item : items) {
                                    favourites.add(item);
                                }
                            }
                    
                            if (favourites.isEmpty()) {
                        %>
                        <div class="empty-message">No flashcards in favourites</div>
                        <%
                            } else {
                                // Pagination parameters
                                int pageSize = 8; // Number of cards per page
                                String pageParam = request.getParameter("page");
                                int currentPage = 1;
                                
                                try {
                                    if (pageParam != null && !pageParam.isEmpty()) {
                                        currentPage = Integer.parseInt(pageParam);
                                        if (currentPage < 1) {
                                            currentPage = 1;
                                        }
                                    }
                                } catch (NumberFormatException e) {
                                    currentPage = 1;
                                }
                                
                                int totalItems = favourites.size();
                                int totalPages = (int) Math.ceil((double) totalItems / pageSize);
                                
                                if (currentPage > totalPages && totalPages > 0) {
                                    currentPage = totalPages;
                                }
                                
                                int startIndex = (currentPage - 1) * pageSize;
                                int endIndex = Math.min(startIndex + pageSize, totalItems);
                                
                                flashcardsetDAO dao = new flashcardsetDAO();
                                
                                // Display only the items for the current page
                                for (int i = startIndex; i < endIndex; i++) {
                                    String fav = favourites.get(i);
                                    FlashcardSet flashcardSet = dao.getFlashcardSetById(Integer.parseInt(fav));
                                    if (flashcardSet != null) {
                        %>
                        <div class="flashcard-box">
                            <h3><%= flashcardSet.getName_of_flashcardset() %></h3>
                            <p><strong>Tag:</strong> <%= flashcardSet.getTags() %></p>
                            <p><strong>Time created:</strong> <%= flashcardSet.getTime_created() %></p>
                            <p><strong>Views:</strong> <%= flashcardSet.getView() %></p>
                            <div class="actions">
                                <a href="updateView?id=<%= flashcardSet.getFlashcard_set_id() %>">Learn</a>
                                <a href="practise.jsp?id=<%= flashcardSet.getFlashcard_set_id() %>" style="background-color: red">Practice</a>
                                <a class="unfav" onclick="removeFromFavourites('<%= flashcardSet.getFlashcard_set_id() %>')" style="background-color: #f39c12">Unfavorite</a>
                            </div>
                        </div>
                        <%
                                    }
                                }
                        %>
                    </div>
                </div>
                
                <!-- Pagination Controls - Now outside the container but still within page-content -->
                <div class="pagination-container">
                    <ul class="pagination">
                        <% if (currentPage > 1) { %>
                            <li><a href="?page=<%= currentPage - 1 %>">Previous</a></li>
                        <% } else { %>
                            <li class="disabled"><a>Previous</a></li>
                        <% } %>
                        
                        <% 
                        // Show a maximum of 5 page numbers
                        int startPage = Math.max(1, currentPage - 2);
                        int endPage = Math.min(totalPages, startPage + 4);
                        
                        // Adjust start page if we're near the end
                        if (endPage - startPage < 4 && startPage > 1) {
                            startPage = Math.max(1, endPage - 4);
                        }
                        
                        for (int i = startPage; i <= endPage; i++) { 
                        %>
                            <li <%= (i == currentPage) ? "class='active'" : "" %>><a href="?page=<%= i %>"><%= i %></a></li>
                        <% } %>
                        
                        <% if (currentPage < totalPages) { %>
                            <li><a href="?page=<%= currentPage + 1 %>">Next</a></li>
                        <% } else { %>
                            <li class="disabled"><a>Next</a></li>
                        <% } %>
                    </ul>
                </div>
                <% } %>
            </div>
        </div>

        <div class="footer">
            <jsp:include page="footer.jsp" />
        </div>

        <script>
            function removeFromFavourites(flashcardSetId) {
                fetch('unfavourite', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded',
                    },
                    body: 'flashcardId=' + flashcardSetId
                })
                        .then(response => response.text())
                        .then(data => {
                            //                alert("Flashcard set removed from favourites!"); // Display notification
                            location.reload(); // Reload the page to show updated favorites
                        })
                        .catch(error => console.error('Error:', error));
            }
        </script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>