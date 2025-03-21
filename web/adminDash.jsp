<%-- 
    Document   : adminDashboard
    Created on : Mar 13, 2025, 9:16:54 AM
    Author     : Phan Sơn
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="jakarta.servlet.http.HttpSession" %>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.List" %>
<%@page import="model.User" %>
<%@page import="model.FlashcardSet" %>
<%@page import="model.StatisticsDAO" %>


<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">

        <title>Console</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">

        <link href="https://fonts.googleapis.com/css?family=Open+Sans:300i,400" rel="stylesheet">
        <link href="https://fonts.googleapis.com/css2?family=Sigmar&display=swap" rel="stylesheet">
        <link href="https://fonts.googleapis.com/css2?family=Kooperativ&display=swap" rel="stylesheet">
        <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined" />
        <!--<link rel="stylesheet" href="Css/consoleStyle.css"/>-->
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

            .right-top-content-container button {
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

            footer p {
                color: white;
                margin: 0.3rem;
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

            .functions {
                position: static;
                height: 500px;
                padding: 50px;
                width: 100%;
                display: flex;
                justify-content: center;
            }

            .function-card {
                display: flex;
                height: 350px;
                width: 250px;
                background-color: #ececec;
                border: #545454 5px solid;
                border-radius: 10px;
                box-shadow: -1rem 0 3rem #9b9b9b;
                transition: 0.4s ease-out;
                position: relative;
                justify-content: center;
            }

            .function-card:not(:first-child) {
                margin-left: 3%;
                margin-bottom: 30px;
            }

            .function-card:hover {
                transform: translateY(-20px);
                transition: 0.4s ease-out;
            }

            .title {
                color: rgb(0, 0, 0);
                font-weight: bold;
                position: absolute;
                margin-top: 10px;
            }

            .description {
                color: rgb(0, 0, 0);
                font-weight: bold;
                position: absolute;
                align-self: flex-end;
            }

            .function-card:hover .stroke {
                stroke-dashoffset: 100;
                transition: 0.6s ease-out;
            }

            footer {
                position: relative;
                background-color: #545454;
                justify-items: center;
                padding-top: 1rem;
                padding-bottom: 1rem;
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

            .copyright {
                text-align: center;
                margin-top: 40px;
                padding-top: 20px;
                border-top: 1px solid rgba(255, 255, 255, 0.2);
            }

            .container {
                width: 90%;
                max-width: 1200px;
                margin: 0 auto;
            }

            body,
            html,
            .container-fluid {
                margin: 0px;
                padding: 0px;
                min-height: 70vh;
            }

            /* Admin Panel Main Styles */
            body {
                font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
                background-color: #f5f7fa;
                color: #333;
                line-height: 1.6;
            }

            .container-fluid {
                padding: 20px;
            }

            /* Tab Navigation */
            .nav-tabs {
                border-bottom: 2px solid #3498db;
                background-color: #fff;
                border-radius: 5px 5px 0 0;
                box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
            }

            .nav-tabs .nav-item {
                margin-bottom: 0;
            }

            .nav-tabs .nav-link {
                border: none;
                color: #555;
                font-weight: 500;
                padding: 12px 20px;
                transition: all 0.3s ease;
            }

            .nav-tabs .nav-link:hover {
                color: #3498db;
                background-color: rgba(52, 152, 219, 0.1);
            }

            .nav-tabs .nav-link.active {
                color: #fff;
                background-color: #3498db;
                border: none;
                border-radius: 4px 4px 0 0;
            }

            /* Tab Content */
            .tab-content {
                background-color: #fff;
                border-radius: 0 0 5px 5px;
                padding: 20px;
                box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
            }

            .tab-pane {
                padding: 15px 5px;
            }

            /* User Cards & Flashcard Set Cards */
            .user-card {
                background-color: #fff;
                border-radius: 8px;
                box-shadow: 0 3px 6px rgba(0, 0, 0, 0.1);
                transition: transform 0.2s ease, box-shadow 0.2s ease;
            }

            .user-card:hover {
                transform: translateY(-3px);
                box-shadow: 0 6px 12px rgba(0, 0, 0, 0.15);
            }

            .user-card p {
                margin-bottom: 8px;
                font-size: 14px;
            }

            .user-card strong {
                color: #2c3e50;
            }

            /* Forms */
            .update-form {
                background-color: #f8f9fa;
                padding: 15px;
                border-radius: 8px;
                margin-top: 15px;
                border-left: 4px solid #3498db;
            }

            .form-control {
                border-radius: 4px;
                border: 1px solid #ced4da;
                padding: 8px 12px;
                transition: border-color 0.15s ease-in-out, box-shadow 0.15s ease-in-out;
            }

            .form-control:focus {
                border-color: #3498db;
                box-shadow: 0 0 0 0.2rem rgba(52, 152, 219, 0.25);
            }

            /* Buttons */
            button, .btn {
                border-radius: 4px;
                padding: 8px 16px;
                font-weight: 500;
                transition: all 0.3s ease;
            }

            button, input[type="submit"], input[type="button"] {
                background-color: #3498db;
                color: white;
                border: none;
                cursor: pointer;
                padding: 8px 16px;
                border-radius: 4px;
            }

            button:hover, input[type="submit"]:hover, input[type="button"]:hover {
                background-color: #2980b9;
            }

            input[value="Delete"] {
                background-color: #e74c3c;
            }

            input[value="Delete"]:hover {
                background-color: #c0392b;
            }

            input[disabled] {
                background-color: #95a5a6;
                cursor: not-allowed;
            }

            /* Search Box */
            .search-user {
                margin-bottom: 20px;
            }

            .search-user form {
                display: flex;
                align-items: center;
            }

            /* Pagination */
            .pagination {
                display: flex;
                justify-content: center;
                margin-top: 20px;
            }

            .pagination a {
                margin: 0 5px;
                min-width: 32px;
                height: 32px;
                display: flex;
                align-items: center;
                justify-content: center;
            }

            .pagination a.active {
                background-color: #3498db;
                color: white;
                border-color: #3498db;
            }

            /* Statistics Tab */
            #tabs-3 {
                padding: 20px;
            }

            #tabs-3 h2 {
                color: #2c3e50;
                margin-bottom: 20px;
                border-bottom: 2px solid #3498db;
                padding-bottom: 10px;
            }

            #tabs-3 p {
                font-size: 16px;
                background-color: #f8f9fa;
                padding: 15px;
                border-radius: 8px;
                margin-bottom: 10px;
                border-left: 4px solid #3498db;
            }

            /* Radio Buttons */
            input[type="radio"] {
                margin-right: 5px;
            }

            .form-check-inline {
                margin-right: 15px;
            }

            /* Error and Log Messages */
            p[style*="color: red"] {
                background-color: #ffebee;
                border-left: 4px solid #e74c3c;
                padding: 10px 15px;
                border-radius: 4px;
                margin-top: 15px;
            }

            p[style*="color: green"] {
                background-color: #e8f5e9;
                border-left: 4px solid #27ae60;
                padding: 10px 15px;
                border-radius: 4px;
                margin-top: 15px;
            }

            /* Responsive Adjustments */
            @media (max-width: 992px) {
                .w-50 {
                    width: 100% !important;
                }

                .user-card .d-flex.flex-row {
                    flex-direction: column !important;
                }

                .col {
                    margin-bottom: 10px;
                }
            }

            @media (max-width: 768px) {
                .nav-tabs {
                    flex-direction: column;
                }

                .nav-tabs .nav-item {
                    width: 100%;
                    text-align: center;
                }

                .nav-tabs .nav-link {
                    border-radius: 0;
                }

                .nav-tabs .nav-link.active {
                    border-radius: 0;
                }

                .form-check-inline {
                    display: block;
                    margin-bottom: 5px;
                }
            }

            /* Create User Form */
            #createForm {
                max-width: 600px;
                margin: 0 auto;
                padding: 20px;
                background-color: #f8f9fa;
                border-radius: 8px;
                box-shadow: 0 3px 10px rgba(0, 0, 0, 0.1);
            }

            #createForm .row {
                align-items: center;
            }

            #createForm label {
                font-weight: 500;
                color: #2c3e50;
            }

            #createForm button {
                width: 100%;
                margin-top: 15px;
                padding: 10px;
                font-size: 16px;
            }

        </style>
    </head>


    <body style="padding: 0px; min-height: 90vh">
        <div class="container-fluid">
            <!--            <div class="row vh-80">
                             Offcanvas for Sliding Content 
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
                        </div>-->

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
                                    <li><a class="dropdown-item" href="/PRJ_SE1938/LogoutServlet">Log out</a></li>
                                </ul>
                            </div>
                        </div>
                    </div>
                </div>

                <div style="min-height: 80px; background-color: #ececec;">
                    Ambatukam ohhhhhh!
                </div>
                <div style="justify-self: center; margin-top: 50px">
                    <a href="dashboard.jsp" class="btn btn-primary">Back to dashboard</a>
                </div> 

                <!-- Main content start here -->
                <section class="container-fluid about-section">
                    <div class="row py-2">
                        <div class="col-lg-12 px-0 py-2">
                            <!-- Nav tabs -->
                            <ul class="nav nav-tabs justify-content-around border-0">
                                <li class="nav-item">
                                    <a class="nav-link active" data-bs-toggle="tab" href="#tabs-0" id="tab-create-user">Create user</a>
                                </li>
                                <li class="nav-item">
                                    <a class="nav-link" data-bs-toggle="tab" href="#tabs-1" id="tab-users">Users Console</a>
                                </li>
                                <li class="nav-item">
                                    <a class="nav-link" data-bs-toggle="tab" href="#tabs-2" id="tab-flashcard">FlashCard Set Console</a>
                                </li>
                                <li class="nav-item">
                                    <a class="nav-link" data-bs-toggle="tab" href="#tabs-3" id="tab-statistic">Statistic</a>
                                </li>
                            </ul>
                        </div>
                        <!-- Tab contents-->
                        <div class="tab-content py-2">

                            <!-- Tab 1 -->
                            <article id="tabs-0" class="tab-pane active">
                                <div id="createForm" class="update-form">
                                    <form action="admin" method="post" class="row">
                                        <input type="hidden" name="userId" required/>

                                        <div class="col-md-6">
                                            <!-- Left Column -->
                                            <!-- Username -->
                                            <div class="mb-3">
                                                <label class="form-label">Username:</label>
                                                <input type="text" name="username" class="form-control" required/>
                                            </div>

                                            <!-- Password -->
                                            <div class="mb-3">
                                                <label class="form-label">Password:</label>
                                                <input type="password" name="password" class="form-control" required/>
                                            </div>

                                            <!-- Email -->
                                            <div class="mb-3">
                                                <label class="form-label">Email:</label>
                                                <input type="email" name="email" class="form-control" required/>
                                            </div>
                                        </div>

                                        <div class="col-md-6">
                                            <!-- Right Column -->
                                            <!-- Phone Number -->
                                            <div class="mb-3">
                                                <label class="form-label">Phone Number:</label>
                                                <input type="text" name="phone_number" class="form-control" required/>
                                            </div>

                                            <!-- Verification Code -->
                                            <div class="mb-3">
                                                <label class="form-label">Verification Code:</label>
                                                <input type="text" name="verificationCode" class="form-control" required/>
                                            </div>

                                            <!-- DOB Code -->
                                            <div class="mb-3">
                                                <label class="form-label">Birthday:</label>
                                                <input type="date" name="dob" class="form-control" required/>
                                            </div>
                                        </div>

                                        <div class="col-12 mt-3 text-center">
                                            <button type="submit" class="btn btn-primary">Create new</button>
                                        </div>
                                    </form>
                                </div>
                            </article>

                            <!-- Tab 2 -->
                            <article id="tabs-1" class="tab-pane fade">
                                <div class="d-flex flex-column justify-content-around align-items-center">
                                    <!-- List all users -->
                                <%
                                String showUsers = request.getParameter("showUsers");
                                if (!"true".equalsIgnoreCase(showUsers)) {
                                %>
                                <a class="btn btn-primary" href="admin?getUser=true&activeTab=1&showUsers=true" id="list-users-btn">List all users</a>
                                <%
                                    } else {
                                %>
                                <div class="search-user mb-3 w-50">
                                    <form action="admin" method="get" class="d-flex flex-column">
                                        <input type="hidden" name="getUser" value="true">
                                        <input type="hidden" name="activeTab" value="1">
                                        <input type="hidden" name="showUsers" value="true">
                                        <input type="text" name="searchStr" placeholder="Search username, phone, or email..." 
                                               class="form-control d-inline-block w-100 me-2" required>
                                        <div class="py-2">
                                            <input type="submit" value="Search" class="btn btn-secondary">
                                            <input type="button" value="Reset" class="btn btn-secondary ms-2" onclick="window.location.href = 'admin?getUser=true&activeTab=1&showUsers=true'">
                                        </div>
                                    </form>
                                </div>
                                <%
                                    }
                                %>

                                <%
                                ArrayList<User> list = (ArrayList<User>) request.getAttribute("userList");

                                String searchStr = request.getParameter("searchStr");
                                ArrayList<User> filteredList = list; // Mặc định là toàn bộ danh sách
                                if (searchStr != null && !searchStr.trim().isEmpty()) {
                                    filteredList = new ArrayList<>();
                                    String query = searchStr.trim().toLowerCase();
                                    for (User u : list) {
                                        // Giả sử các getter trả về chuỗi và không null
                                        if (u.getUsername().toLowerCase().contains(query) ||
                                            u.getEmail().toLowerCase().contains(query) ||
                                            u.getPhoneNumber().toLowerCase().contains(query)) {
                                            filteredList.add(u);
                                        }
                                    }
                                }
                        
                                int pageSize = 4;

                                int currentPage = 1;
                                if (request.getParameter("page") != null) {
                                    try {
                                        currentPage = Integer.parseInt(request.getParameter("page"));
                                        if (currentPage < 1) {
                                            currentPage = 1;
                                        }
                                    } catch(NumberFormatException e) {
                                        currentPage = 1;
                                    }
                                }

                                int start = (currentPage - 1) * pageSize;
                                int end = 0;
                                if(filteredList != null) {
                                    end = Math.min(filteredList.size(), start + pageSize);
                                }

                                ArrayList<User> usersToShow = new ArrayList<>();
                                if (filteredList != null && filteredList.size() > 0 && start < filteredList.size()) {
                                    usersToShow.addAll(filteredList.subList(start, end));
                                }
                                %>
                                <div class="d-flex flex-row flex-wrap w-100">
                                    <%
                                    if (usersToShow.size() > 0) {
                                        for (User u : usersToShow) {
                                    %>
                                    <div class="w-50">
                                        <div class="user-card  mb-3 p-2 border rounded">
                                            <div class="d-flex flex-wrap">
                                                <div class="d-flex flex-row w-100">
                                                    <div class="col px-2">
                                                        <p><strong>ID:</strong> <%= u.getUserId() %></p>
                                                        <p><strong>Username:</strong> <%= u.getUsername() %></p>
                                                        <p><strong>Password:</strong> <%= u.getPassword() %></p>
                                                        <p><strong>Email:</strong> <%= u.getEmail() %></p>
                                                        <p><strong>Phone:</strong> <%= u.getPhoneNumber() %></p>
                                                        <p><strong>DOB:</strong> <%= u.getDob() %></p>
                                                        <p><strong>Role:</strong> <%= u.isRole() ? "Admin":"Standard" %></p>

                                                    </div>
                                                    <div class="col px-2">
                                                        <p><strong>VIP:</strong> <%= u.isVipStatus() ? "Yes":"No" %></p>
                                                        <%
                                                        if (u.isVipStatus()){
                                                        %>
                                                        <p><strong>Type:</strong> <%= u.getVipType() %></p>
                                                        <p><strong>VIP Duration: </strong><%= u.getVipStartDate()%> - <%= u.getVipEndDate()%></p>
                                                        <%
                                                        }
                                                        %>
                                                        <p><strong>Verification Code:</strong> <%= u.getVerificationCode() %></p>
                                                        <p><strong>Verification Status:</strong> <%= u.isIsVerification() ? "Yes":"No" %></p> 
                                                    </div>
                                                </div>
                                                <div class="d-flex align-items-center w-100">
                                                    <button class="col px-2" onclick="toggleUpdateForm('<%= u.getUserId() %>')">Update</button>
                                                    <form class="col px-2 d-flex justify-content-center" action="admin" method="post">
                                                        <input type="hidden" name="_method" value="delete">
                                                        <input type="hidden" name="deleteUser" value="<%= u.getUserId() %>">
                                                        <input type="submit" value="Delete" <%= (((User)session.getAttribute("user")).getUserId() == u.getUserId()) ? "disabled":""%> >
                                                    </form>
                                                </div>

                                            </div>
                                            <div id="updateForm-<%= u.getUserId() %>" class="update-form" style="display: none;">
                                                <form action="admin" method="post">
                                                    <input type="hidden" name="_method" value="put">
                                                    <input type="hidden" name="updateUser" value="user">
                                                    <input type="hidden" name="userId" value="<%= u.getUserId() %>"/>

                                                    <!-- Username -->
                                                    <div class="row mb-3">
                                                        <div class="col  mb-3">
                                                            <label>Username:</label>
                                                        </div>
                                                        <div class="col  mb-3">
                                                            <input type="text" name="username" value="<%= u.getUsername() %>" class="form-control"/>
                                                        </div>
                                                    </div>

                                                    <!-- Pass -->
                                                    <div class="row mb-3">
                                                        <div class="col  mb-3">
                                                            <label>Password:</label>
                                                        </div>
                                                        <div class="col  mb-3">
                                                            <input type="password" name="password" value="<%= u.getPassword() %>" class="form-control"/>
                                                        </div>
                                                    </div>
                                                    <!-- Email -->
                                                    <div class="row mb-3">
                                                        <div class="col  mb-3">
                                                            <label>Email:</label>
                                                        </div>
                                                        <div class="col  mb-3">
                                                            <input type="email" name="email" value="<%= u.getEmail() %>" class="form-control"/>
                                                        </div>
                                                    </div>

                                                    <!-- Phone -->
                                                    <div class="row mb-3">
                                                        <div class="col  mb-3">
                                                            <label>Phone Number:</label>
                                                        </div>
                                                        <div class="col  mb-3">
                                                            <input type="text" name="phoneNumber" value="<%= u.getPhoneNumber() %>" class="form-control"/>
                                                        </div>
                                                    </div>


                                                    <div class="row mb-3">
                                                        <div class="col mb-3">
                                                            <label>DOB:</label>
                                                        </div>
                                                        <div class="col mb-3">
                                                            <input type="date" name="dob" value="<%= u.getDob() %>" class="form-control"/>
                                                        </div>
                                                    </div>

                                                    <div class="row mb-3">
                                                        <div class="col mb-3">
                                                            <label>Verification Code:</label>
                                                        </div>
                                                        <div class="col mb-3">
                                                            <input type="text" name="verificationCode" value="<%= u.getVerificationCode() %>" class="form-control"/><br>
                                                        </div>
                                                    </div>

                                                    <!-- VIP Status -->
                                                    <div class="row mb-3">
                                                        <div class="col  mb-3">
                                                            <label>VIP Status:</label>
                                                        </div>
                                                        <div class="col  mb-3">
                                                            <input type="radio" name="vipStatus" id="vipYes-<%= u.getUserId() %>" value="Yes" <%= u.isVipStatus() ? "checked" : "" %> />
                                                            <label for="vipYes-<%= u.getUserId() %>">Yes</label>
                                                        </div>
                                                        <div class="col  mb-3">
                                                            <input type="radio" name="vipStatus" id="vipNo-<%= u.getUserId() %>" value="No" <%= !u.isVipStatus() ? "checked" : "" %> />
                                                            <label for="vipNo-<%= u.getUserId() %>">No</label>
                                                        </div>
                                                    </div>

                                                    <!-- VIP Dates: hiển thị mặc định theo trạng thái của u.isVipStatus() -->
                                                    <div id="vipDates-<%= u.getUserId() %>" style="display: <%= u.isVipStatus() ? "block" : "none" %>;">
                                                        <!-- Hàng mới với 3 lựa chọn radio -->
                                                        <div class="row mb-3">
                                                            <div class="col mb-3">
                                                                <label>VIP Type</label>
                                                            </div>
                                                            <div class="col mb-3">
                                                                <input type="radio" name="vipType" id="opt1-<%= u.getUserId() %>" value="vip1" <%= ("vip1".equals(u.getVipType())) ? "checked" : ""%>/>
                                                                <label for="opt1-<%= u.getUserId() %>">VIP 1</label>
                                                            </div>
                                                            <div class="col mb-3">
                                                                <input type="radio" name="vipType" id="opt2-<%= u.getUserId() %>" value="vip2" <%= ("vip2".equals(u.getVipType())) ? "checked" : ""%>/>
                                                                <label for="opt2-<%= u.getUserId() %>">VIP 2</label>
                                                            </div>
                                                            <div class="col mb-3">
                                                                <input type="radio" name="vipType" id="opt3-<%= u.getUserId() %>" value="vip3" <%= ("vip3".equals(u.getVipType())) ? "checked" : ""%>/>
                                                                <label for="opt3-<%= u.getUserId() %>">VIP 3</label>
                                                            </div>
                                                        </div>
                                                        <!-- Hàng hiện có: Started VIP Date -->
                                                        <div class="row mb-3">
                                                            <div class="col mb-3">
                                                                <label>Started VIP Date:</label>
                                                            </div>
                                                            <div class="col mb-3">
                                                                <input type="date" name="vipStartDate" value="<%= u.getVipStartDate() %>" class="form-control"/>
                                                            </div>
                                                        </div>
                                                        <!-- Hàng hiện có: End VIP Date -->
                                                        <div class="row mb-3">
                                                            <div class="col mb-3">
                                                                <label>End VIP Date:</label>
                                                            </div>
                                                            <div class="col mb-3">
                                                                <input type="date" name="vipEndDate" value="<%= u.getVipEndDate() %>" class="form-control"/>
                                                            </div>
                                                        </div>

                                                    </div>


                                                    <!-- Role -->
                                                    <div class="row mb-3">
                                                        <div class="col  mb-3">
                                                            <label>Role:</label>
                                                        </div>
                                                        <div class="col  mb-3">
                                                            <input type="radio" name="role" value="admin" <%= u.isRole() ? "checked" : "" %> />
                                                            <label for="admin">Admin</label>
                                                        </div>
                                                        <div class="col  mb-3">
                                                            <input type="radio" name="role" value="standard" <%= !u.isRole() ? "checked" : "" %> />
                                                            <label for="standard">Standard</label><br>
                                                        </div>
                                                    </div>

                                                    <!-- Verification Status -->
                                                    <div class="row mb-3">
                                                        <div class="col  mb-3">
                                                            <label>Is Verification:</label>
                                                        </div>
                                                        <div class="col  mb-3">
                                                            <input type="radio" name="isVerification" value="true" <%= u.isIsVerification() ? "checked" : "" %> />
                                                            <label for="true">Yes</label>
                                                        </div>
                                                        <div class="col  mb-3">
                                                            <input type="radio" name="isVerification" value="false" <%= !u.isIsVerification() ? "checked" : "" %> />
                                                            <label for="false">No</label>
                                                        </div>
                                                    </div>



                                                    <button type="submit">Save Changes</button>
                                                </form>

                                            </div>

                                        </div>
                                    </div>
                                    <%
                                        }
                                    } else {
                                    %>
                                    <p>No users found.</p>

                                    <%
                                    }
                                    %>
                                </div>
                                <div class="pagination mt-3">
                                    <%
                                        int totalPages = (filteredList != null && filteredList.size() > 0) ? (int) Math.ceil((double) filteredList.size() / pageSize) : 1;
                                        for (int i = 1; i <= totalPages; i++) {
                                    %>
                                    <a class="btn btn-outline-primary me-1 <%= (i == currentPage ? "active" : "") %>" 
                                       href="admin?getUser=true&activeTab=1&showUsers=true&page=<%= i %><%= (searchStr != null ? "&searchStr=" + searchStr : "") %>"><%= i %></a>
                                    <%
                                        }
                                    %>
                                </div>
                            </div>
                        </article>

                        <!-- Tab 3 -->
                        <article id="tabs-2" class="tab-pane fade">
                            <div class="d-flex flex-column justify-content-around align-items-center">
                                <!-- List all Flashcard Set -->
                                <%
                                String showFS = request.getParameter("showFS");
                                if (!"true".equalsIgnoreCase(showFS)) {
                                %>
                                <a class="btn btn-primary" href="admin?getFS=true&activeTab=2&showFS=true" id="list-fs-btn">List all FlashCard Set</a>
                                <%
                                    } else {
                                %>
                                <div class="search-user mb-3 w-50">
                                    <form action="admin" method="get" class="d-flex flex-column">
                                        <input type="hidden" name="getFS" value="true">
                                        <input type="hidden" name="activeTab" value="2">
                                        <input type="hidden" name="showFS" value="true">
                                        <input type="text" name="fs_searchStr" placeholder="Search FlashSet Card..." 
                                               class="form-control d-inline-block w-100 me-2" required>
                                        <div class="py-2">
                                            <input type="submit" value="Search" class="btn btn-secondary">
                                            <input type="button" value="Reset" class="btn btn-secondary ms-2" onclick="window.location.href = 'admin?getFS=true&activeTab=2&showFS=true'">
                                        </div>
                                    </form>
                                </div>
                                <%
                                    }
                                %>

                                <%
                                ArrayList<FlashcardSet> fs_list = (ArrayList<FlashcardSet>) request.getAttribute("fsList");

                                String fs_searchStr = request.getParameter("fs_searchStr");
                                ArrayList<FlashcardSet> fs_filteredList = fs_list; // Mặc định là toàn bộ danh sách
                                if (fs_searchStr != null && !fs_searchStr.trim().isEmpty()) {
                                    fs_filteredList = new ArrayList<>();
                                    String fs_query = fs_searchStr.trim().toLowerCase();
                                    for (FlashcardSet fs : fs_list) {
                                        // Giả sử các getter trả về chuỗi và không null
                                        if (fs.getName_of_flashcardset().toLowerCase().contains(fs_query) ||
                                            fs.getTags().toLowerCase().contains(fs_query)){
                                            fs_filteredList.add(fs);
                                        }
                                    }
                                }
                        
                                int fs_pageSize = 4;

                                int fs_currentPage = 1;
                                if (request.getParameter("page") != null) {
                                    try {
                                        fs_currentPage = Integer.parseInt(request.getParameter("page"));
                                        if (fs_currentPage < 1) {
                                            fs_currentPage = 1;
                                        }
                                    } catch(NumberFormatException e) {
                                        fs_currentPage = 1;
                                    }
                                }

                                int fs_start = (fs_currentPage - 1) * fs_pageSize;
                                int fs_end = 0;
                                if(fs_filteredList != null) {
                                    fs_end = Math.min(fs_filteredList.size(), fs_start + fs_pageSize);
                                }

                                ArrayList<FlashcardSet> fsToShow = new ArrayList<>();
                                if (fs_filteredList != null && fs_filteredList.size() > 0 && fs_start < fs_filteredList.size()) {
                                    fsToShow.addAll(fs_filteredList.subList(fs_start, fs_end));
                                }
                                %>
                                <div class="d-flex flex-row flex-wrap w-100">
                                    <%
                                    if (fsToShow.size() > 0) {
                                        for (FlashcardSet fs : fsToShow) {
                                    %>
                                    <div class="w-50">
                                        <div class="user-card  mb-3 p-2 border rounded">
                                            <div class="d-flex flex-wrap">
                                                <div class="d-flex flex-row w-100">
                                                    <div class="col px-2">
                                                        <p><strong>ID:</strong> <%= fs.getFlashcard_set_id() %></p>
                                                        <p><strong>Time Created:</strong> <%= fs.getTime_created() %></p>
                                                        <p><strong>User ID:</strong> <%= fs.getUser_id() %></p>
                                                        <p><strong>Name:</strong> <%= fs.getName_of_flashcardset() %></p>
                                                        <p><strong>Status:</strong> <%= fs.getStatus() %></p>
                                                    </div>
                                                    <div class="col px-2">
                                                        <p><strong>Score:</strong> <%= fs.getScore() %></p>
                                                        <p><strong>Tags:</strong> <%= fs.getTags() %></p>
                                                        <p><strong>Visibility:</strong> <%= fs.getVisibility() %></p>
                                                        <p><strong>Views:</strong> <%= fs.getView() %></p>
                                                    </div>
                                                </div>
                                                <div class="d-flex align-items-center w-100">
                                                    <button class="col px-2" onclick="toggleUpdateForm('<%= fs.getFlashcard_set_id() %>')">Update</button>
                                                    <form class="col px-2 d-flex justify-content-center" action="admin" method="post">
                                                        <input type="hidden" name="_method" value="delete">
                                                        <input type="hidden" name="deleteFS" value="<%= fs.getFlashcard_set_id() %>">
                                                        <input type="submit" value="Delete">
                                                    </form>
                                                </div>

                                            </div>
                                            <div id="updateForm-<%= fs.getFlashcard_set_id() %>" class="update-form" style="display: none;">
                                                <form action="admin" method="post">
                                                    <input type="hidden" name="_method" value="put">
                                                    <input type="hidden" name="updateFS" value="fs">
                                                    <input type="hidden" name="fsId" value="<%= fs.getFlashcard_set_id() %>"/>

                                                    <!-- Name of Flashcard Set -->
                                                    <div class="row mb-3">
                                                        <div class="col">
                                                            <label for="name-<%= fs.getFlashcard_set_id() %>" class="form-label">Name of Flashcard Set:</label>
                                                        </div>
                                                        <div class="col">
                                                            <input type="text" id="name-<%= fs.getFlashcard_set_id() %>" name="name_of_flashcardset" 
                                                                   value="<%= fs.getName_of_flashcardset() %>" class="form-control" required>
                                                        </div>
                                                    </div>

                                                    <!-- Status -->
                                                    <div class="row mb-3">
                                                        <div class="col">
                                                            <label class="form-label">Status:</label>
                                                        </div>
                                                        <div class="col">
                                                            <div class="form-check form-check-inline">
                                                                <input class="form-check-input" type="radio" name="status" id="statusNew-<%= fs.getFlashcard_set_id() %>" value="New" 
                                                                       <%= "New".equals(fs.getStatus()) ? "checked" : "" %>>
                                                                <label class="form-check-label" for="statusNew-<%= fs.getFlashcard_set_id() %>">New</label>
                                                            </div>
                                                            <div class="form-check form-check-inline">
                                                                <input class="form-check-input" type="radio" name="status" id="statusLearning-<%= fs.getFlashcard_set_id() %>" value="Learning" 
                                                                       <%= "Learning".equals(fs.getStatus()) ? "checked" : "" %>>
                                                                <label class="form-check-label" for="statusLearning-<%= fs.getFlashcard_set_id() %>">Learning</label>
                                                            </div>
                                                            <div class="form-check form-check-inline">
                                                                <input class="form-check-input" type="radio" name="status" id="statusKnown-<%= fs.getFlashcard_set_id() %>" value="Known" 
                                                                       <%= "Known".equals(fs.getStatus()) ? "checked" : "" %>>
                                                                <label class="form-check-label" for="statusKnown-<%= fs.getFlashcard_set_id() %>">Known</label>
                                                            </div>
                                                            <div class="form-check form-check-inline">
                                                                <input class="form-check-input" type="radio" name="status" id="statusForgotten-<%= fs.getFlashcard_set_id() %>" value="Forgotten" 
                                                                       <%= "Forgotten".equals(fs.getStatus()) ? "checked" : "" %>>
                                                                <label class="form-check-label" for="statusForgotten-<%= fs.getFlashcard_set_id() %>">Forgotten</label>
                                                            </div>
                                                        </div>
                                                    </div>

                                                    <!-- Score -->
                                                    <div class="row mb-3">
                                                        <div class="col">
                                                            <label for="score-<%= fs.getFlashcard_set_id() %>" class="form-label">Score:</label>
                                                        </div>
                                                        <div class="col">
                                                            <input type="number" step="0.01" id="score-<%= fs.getFlashcard_set_id() %>" name="score" 
                                                                   value="<%= fs.getScore() %>" class="form-control" required>
                                                        </div>
                                                    </div>

                                                    <!-- Tags -->
                                                    <div class="row mb-3">
                                                        <div class="col">
                                                            <label for="tags-<%= fs.getFlashcard_set_id() %>" class="form-label">Tags:</label>
                                                        </div>
                                                        <div class="col">
                                                            <input type="text" id="tags-<%= fs.getFlashcard_set_id() %>" name="tags" 
                                                                   value="<%= fs.getTags() %>" class="form-control" required>
                                                        </div>
                                                    </div>

                                                    <!-- Visibility -->
                                                    <div class="row mb-3">
                                                        <div class="col">
                                                            <label class="form-label">Visibility:</label>
                                                        </div>
                                                        <div class="col">
                                                            <div class="form-check form-check-inline">
                                                                <input class="form-check-input" type="radio" name="visibility" id="visEveryone-<%= fs.getFlashcard_set_id() %>" value="Everyone" 
                                                                       <%= "Everyone".equals(fs.getVisibility()) ? "checked" : "" %>>
                                                                <label class="form-check-label" for="visEveryone-<%= fs.getFlashcard_set_id() %>">Everyone</label>
                                                            </div>
                                                            <div class="form-check form-check-inline">
                                                                <input class="form-check-input" type="radio" name="visibility" id="visOnlyMe-<%= fs.getFlashcard_set_id() %>" value="Only me" 
                                                                       <%= "Only me".equals(fs.getVisibility()) ? "checked" : "" %>>
                                                                <label class="form-check-label" for="visOnlyMe-<%= fs.getFlashcard_set_id() %>">Only me</label>
                                                            </div>
                                                        </div>
                                                    </div>

                                                    <button type="submit">Save Changes</button>
                                                </form>

                                            </div>

                                        </div>
                                    </div>
                                    <%
                                        }
                                    } else {
                                    %>
                                    <p>No Flash Set found.</p>

                                    <%
                                    }
                                    %>
                                </div>
                                <div class="pagination mt-3">
                                    <%
                                        int fs_totalPages = (fs_filteredList != null && fs_filteredList.size() > 0) ? (int) Math.ceil((double) fs_filteredList.size() / fs_pageSize) : 1;
                                        for (int i = 1; i <= fs_totalPages; i++) {
                                    %>
                                    <a class="btn btn-outline-primary me-1 <%= (i == fs_currentPage ? "active" : "") %>" 
                                       href="admin?getFS=true&activeTab=2&showFS=true&page=<%= i %><%= (fs_searchStr != null ? "&fs_searchStr=" + fs_searchStr : "") %>"><%= i %></a>
                                    <%
                                        }
                                    %>
                                </div>
                            </div>
                        </article>
                        <!--  tab 4-->
                        <%
                         StatisticsDAO statsDAO = new StatisticsDAO();
                         int totalUsers = statsDAO.getTotalUsers();
                         int totalFlashcardSets = statsDAO.getTotalFlashcardSets();
                         int totalVipUsers = statsDAO.getTotalVipUsers();
                        %>        
                        <article id="tabs-3" class="tab-pane">
                            <h2>System Statistics</h2>
                            <p><strong>Total Users:</strong> <%= totalUsers %></p>
                            <p><strong>Total Flashcard Sets:</strong> <%= totalFlashcardSets %></p>
                            <p><strong>Total VIP Accounts:</strong> <%= totalVipUsers %></p>
                        </article>



                    </div>
                </div>
            </section>
            <!-- Check log and error -->



            <%
            String error = (String)request.getAttribute("Error");
            String log = (String) request.getAttribute("Log");
            if (error != null){
            %>
            <p style="color: red"><%=error%></p>
            <%
                }
            if (log != null){
            %>
            <p style="color: green"><%=log%></p>
            <%
                        }
            %>
        </div>
        <div class="footer">
            <jsp:include page="footer.jsp" />
        </div>
        <!-- Bootstrap 5 CDN -->
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js" integrity="sha384-MrcW6ZMFYlzcLA8Nl+NtUVF0sA7MsXsP1UyJoMp4YLEuNSfAP+JcXn/tWtIaxVXM" crossorigin="anonymous"></script>
        <script>
                                                        function toggleUpdateForm(userId) {
                                                            let form = document.getElementById("updateForm-" + userId);
                                                            if (form.style.display === "none") {
                                                                form.style.display = "block";
                                                            } else {
                                                                form.style.display = "none";
                                                            }
                                                        }
                                                        function deleteUser(userId) {
                                                            if (confirm("Are you sure you want to delete this user?")) {
                                                                window.location.href = "deleteUser?userId=" + userId;
                                                            }
                                                        }
                                                        document.addEventListener("DOMContentLoaded", function () {
                                                            const params = new URLSearchParams(window.location.search);
                                                            const activeTab = params.get("activeTab");
                                                            if (activeTab !== null) {
                                                                // Tìm phần tử nav-link có href trỏ đến tab tương ứng
                                                                const tabTrigger = document.querySelector('.nav-link[href="#tabs-' + activeTab + '"]');
                                                                if (tabTrigger) {
                                                                    // Sử dụng Bootstrap Tab để hiển thị tab đó
                                                                    var tab = new bootstrap.Tab(tabTrigger);
                                                                    tab.show();
                                                                }
                                                            }
                                                        });
                                                        // Hàm toggle riêng cho từng user, dựa trên userId
                                                        function toggleVipDatesForUser(userId) {
                                                            var vipYes = document.getElementById("vipYes-" + userId);
                                                            var vipDates = document.getElementById("vipDates-" + userId);
                                                            if (vipYes && vipDates) {
                                                                vipDates.style.display = vipYes.checked ? "block" : "none";
                                                            }
                                                        }

                                                        document.addEventListener("DOMContentLoaded", function () {
                                                            // Lấy tất cả các update form trên trang active (chỉ các user hiển thị trong trang phân trang hiện tại)
                                                            var updateForms = document.querySelectorAll("[id^='updateForm-']");
                                                            updateForms.forEach(function (form) {
                                                                // Lấy userId từ ID của form (ví dụ: "updateForm-5")
                                                                var parts = form.id.split("-");
                                                                var userId = parts[1];

                                                                // Gắn sự kiện change cho các radio button có name "vipStatus" trong form này
                                                                var vipRadios = form.querySelectorAll("input[name='vipStatus']");
                                                                vipRadios.forEach(function (radio) {
                                                                    radio.addEventListener("change", function () {
                                                                        toggleVipDatesForUser(userId);
                                                                    });
                                                                });

                                                                // Gọi toggle ban đầu cho form
                                                                toggleVipDatesForUser(userId);
                                                            });
                                                        });
                                                        document.addEventListener("DOMContentLoaded", function () {
                                                            // Bắt sự kiện khi người dùng click vào một tab
                                                            document.querySelectorAll('.nav-link').forEach(tab => {
                                                                tab.addEventListener("click", function () {
                                                                    setTimeout(() => { // Đợi tab chuyển đổi hoàn tất
                                                                        if (this.id === "tab-users") {
                                                                            document.getElementById("list-users-btn").click();  // Kích hoạt List Users
                                                                        }
                                                                        if (this.id === "tab-flashcard") {
                                                                            document.getElementById("list-fs-btn").click();  // Kích hoạt Load FlashCard Sets
                                                                        }
                                                                    }, 200); // Chờ 200ms để đảm bảo tab được hiển thị trước khi kích hoạt button
                                                                });
                                                            });
                                                        });

        </script>
    </body>

</html>
