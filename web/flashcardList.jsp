<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.List" %>
<%@page import="model.FlashCard" %>
<%@page import="model.flashcardDAO" %>
<%@page import="model.flashcardsetDAO" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <title>Flash Card Editor - CARDLORD</title>
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

            /* Flashcard Specific Styles */
            .flashcard-container {
                padding: 30px;
                margin-top: 80px;
                background-color: #f8f9fa;
                border-radius: 10px;
                width: 100vw;
            }

            .flashcard-grid {
                display: grid;
                grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
                gap: 20px;
                margin-top: 30px;
            }

            .flashcard {
                background-color: white;
                border-radius: 10px;
                box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
                padding: 20px;
                transition: transform 0.3s ease, box-shadow 0.3s ease;
                position: relative;
            }

            .flashcard:hover {
                transform: translateY(-5px);
                box-shadow: 0 8px 16px rgba(0, 0, 0, 0.2);
            }

            .flashcard-id {
                position: absolute;
                top: 10px;
                right: 10px;
                background-color: #545454;
                color: white;
                padding: 3px 8px;
                border-radius: 20px;
                font-size: 12px;
            }

            .flashcard-term {
                font-weight: bold;
                font-size: 1.2rem;
                margin-bottom: 10px;
                padding-bottom: 10px;
                border-bottom: 1px solid #eee;
                cursor: pointer;
                overflow: hidden;         /* Hides overflow text */
                text-overflow: ellipsis;

            }

            .flashcard-definition {
                margin-top: 10px;
                margin-bottom: 15px;
                min-height: 80px;
                cursor: pointer;
                overflow: hidden;         /* Hides overflow text */
                text-overflow: ellipsis;
            }

            .flashcard-actions {
                display: flex;
                justify-content: flex-end;
                margin-top: 10px;
            }

            .btn-create {
                background-color: #545454;
                color: white;
                border: none;
                padding: 10px;
                border-radius: 5px;
                cursor: pointer;
                margin-bottom: 20px;
                transition: background-color 0.3s ease;
            }

            .btn-create:hover {
                background-color: #333;
            }

            .flashcard-form {
                margin-bottom: 30px;
                padding: 25px;
                background-color: white;
                border-radius: 10px;
                display: none;
                box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
            }

            .flashcard-form input[type="text"],
            .flashcard-form textarea {
                width: 100%;
                padding: 12px;
                margin-bottom: 15px;
                border: 1px solid #ddd;
                border-radius: 5px;
                transition: border-color 0.3s ease;
            }

            .flashcard-form input[type="text"]:focus,
            .flashcard-form textarea:focus {
                border-color: #545454;
                outline: none;
            }

            .flashcard-form input[type="submit"] {
                background-color: #545454;
                color: white;
                border: none;
                padding: 12px 20px;
                border-radius: 5px;
                cursor: pointer;
                transition: background-color 0.3s ease;
            }

            .flashcard-form input[type="submit"]:hover {
                background-color: #333;
            }

            .btn-delete {
                background-color: #dc3545;
                color: white;
                border: none;
                padding: 8px 15px;
                border-radius: 5px;
                cursor: pointer;
                transition: background-color 0.3s ease;
            }

            .btn-delete:hover {
                background-color: #c82333;
            }

            .editable-input {
                width: 100%;
                padding: 8px;
                border: 1px solid #ddd;
                border-radius: 4px;
            }

            .section-title {
                color: #333;
                margin-bottom: 25px;
                position: relative;
                padding-bottom: 10px;
            }

            .section-title:after {
                content: '';
                position: absolute;
                bottom: 0;
                left: 0;
                width: 75vw;
                height: 3px;
                background-color: #545454;
            }

            .empty-state {
                text-align: center;
                padding: 50px;
                color: #777;
            }
        </style>
    </head>

    <body style="padding: 0px; min-height: 90vh; background-color: #f8f9fa;">
        <%
            flashcardDAO dao = new flashcardDAO();
            flashcardsetDAO setDAO = new flashcardsetDAO();
            int flashcardSetId = Integer.parseInt(request.getParameter("id"));
            List<FlashCard> flashcards = dao.getFlashcard(flashcardSetId);
            String flashcardSetName = setDAO.getFlashcardName(flashcardSetId);
            request.setAttribute("flashcardSetName", flashcardSetName);
        %>

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

                <!-- Main Content Area (Flashcard Editor) -->
                <div class="container flashcard-container" style="min-height: 60vh">
                    <h2 class="section-title">Flashcard Set: <%= flashcardSetName %></h2>

                <!-- Create Flashcard Button -->
                <button id="button_for_hidden" class="btn-create" style="font-size: 22px; font-weight: lighter">
                    <i class="material-symbols-outlined" style="transform: translateY(4px);">add</i>  Create New Flashcard
                </button>

                <!-- Create Flashcard Form -->
                <form id="enable-form" class="flashcard-form" action="createFlashcardServlet" method="POST">
                    <div class="mb-3">
                        <label for="term" class="form-label">Term:</label>
                        <textarea class="form-control" id="term" name="term" required placeholder="Term"></textarea>
                    </div>
                    <div class="mb-3">
                        <label for="flashcard_definition" class="form-label">Definition:</label>
                        <textarea class="form-control" id="flashcard_definition" name="flashcard_definition" rows="3" required placeholder="Definition"></textarea>
                    </div>
                    <input type="hidden" name="flashcard_set_id" value="<%= request.getParameter("id") %>">
                    <input type="submit" name="submit" value="Create" class="btn btn-primary">
                </form>

                <!-- Flashcard Grid Layout -->
                <div class="flashcard-grid">
                    <% if (flashcards.isEmpty()) { %>
                    <div class="empty-state">
                        <h4>No flashcards yet</h4>
                        <p>Click the "Create New Flashcard" button to get started!</p>
                    </div>
                    <% } else { %>
                    <% for(FlashCard flashcard : flashcards) { %>
                    <div class="flashcard" id="card-<%= flashcard.getFlashcard_id() %>">
                        <div class="flashcard-id">#<%= flashcard.getFlashcard_id() %></div>

                        <div class="flashcard-term editable" data-id="<%= flashcard.getFlashcard_id() %>" data-field="term">
                            <%= flashcard.getTerm() %>
                        </div>

                        <div class="flashcard-definition editable" data-id="<%= flashcard.getFlashcard_id() %>" data-field="flashcard_definition">
                            <%= flashcard.getFlashcard_definition() %>
                        </div>

                        <div class="flashcard-actions">
                            <form action="deleteFlashcardServlet" method="GET">
                                <input type="hidden" name="flashcard_id" value="<%= flashcard.getFlashcard_id() %>">
                                <input type="hidden" name="flashcard_set_id" value="<%= flashcardSetId %>">
                                <button type="submit" class="btn-delete" onclick="return confirm('Are you sure you want to delete this flashcard?');">
                                    Delete
                                </button>
                            </form>
                        </div>
                    </div>
                    <% } %>
                    <% } %>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- Footer -->
<div class="footer">
    <jsp:include page="footer.jsp" />
</div>

<!-- Scripts -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script>
                                    document.addEventListener("DOMContentLoaded", function () {
                                        // Toggle create form visibility
                                        const createButton = document.getElementById("button_for_hidden");
                                        const createForm = document.getElementById("enable-form");

                                        createButton.addEventListener("click", function () {
                                            if (createForm.style.display === "block") {
                                                createForm.style.display = "none";
                                            } else {
                                                createForm.style.display = "block";
                                            }
                                        });

                                        // Make card content editable
                                        document.querySelectorAll(".editable").forEach(element => {
                                            element.addEventListener("click", function () {
                                                if (this.querySelector("input") || this.querySelector("textarea"))
                                                    return; // Avoid creating multiple inputs

                                                let originalText = this.innerText.trim();
                                                let isDefinition = this.classList.contains("flashcard-definition");

                                                let input;
                                                input = document.createElement("textarea");
                                                input.rows = 3;

                                                input.className = "editable-input";
                                                input.value = originalText;
                                                this.innerHTML = "";
                                                this.appendChild(input);
                                                input.focus();

                                                const handleSave = () => {
                                                    saveChanges(element, originalText);
                                                };

                                                input.addEventListener("blur", handleSave);

                                                input.addEventListener("keypress", function (e) {
                                                    if (e.key === "Enter" && !isDefinition) {
                                                        e.preventDefault();
                                                        handleSave();
                                                    }
                                                });
                                            });
                                        });

                                        function saveChanges(element, originalText) {
                                            let input = element.querySelector("input") || element.querySelector("textarea");
                                            if (!input)
                                                return;

                                            let newValue = input.value.trim();
                                            let flashcardId = element.getAttribute("data-id");
                                            let field = element.getAttribute("data-field");

                                            if (!flashcardId || !field) {
                                                alert("Error: Flashcard ID or field not found!");
                                                return;
                                            }

                                            if (newValue === originalText) {
                                                element.innerHTML = originalText;
                                                return;
                                            }

                                            // Send update request
                                            fetch("updateFlashcardServlet", {
                                                method: "POST",
                                                headers: {"Content-Type": "application/x-www-form-urlencoded"},
                                                body: new URLSearchParams({
                                                    flashcard_id: flashcardId,
                                                    field: field,
                                                    value: newValue
                                                })
                                            })
                                                    .then(response => response.text())
                                                    .then(data => {
                                                        if (data.trim() === "success") {
                                                            element.innerHTML = newValue;
                                                        } else {
                                                            element.innerHTML = originalText; // Restore if error
                                                            alert("Update failed! Please try again.");
                                                        }
                                                    })
                                                    .catch(error => {
                                                        element.innerHTML = originalText; // Restore if error
                                                        alert("Connection error!");
                                                    });
                                        }
                                    });


</script>
</body>
</html>