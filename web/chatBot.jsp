<!DOCTYPE html>
<html lang="en" dir="ltr">
    <head>
        <meta charset="utf-8" />
        <title>Chatbot</title>
        <link rel="stylesheet" href="Css/style.css" />
        <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
        <!-- Google Fonts Link For Icons -->
        <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined:opsz,wght,FILL,GRAD@48,400,0,0" />
        <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Material+Symbols+Rounded:opsz,wght,FILL,GRAD@48,400,1,0" />
        <link href="https://fonts.googleapis.com/css2?family=Sigmar&display=swap" rel="stylesheet">

        <style>
            body {
                overflow: hidden;
            }
            .logo a h1{
                font-family: 'Sigmar';
                text-shadow:
                    -2px -2px 0 black,
                    2px -2px 0 black,
                    -2px  2px 0 black,
                    2px  2px 0 black;
                color: white;
                font-size: 80px;
            }
            .logo a{
                text-decoration: none;
            }
        </style>

    </head>
        <body style="background-image: url(icon_image/chatbot-bg.png); background-size: cover">
                <div class="logo" style="margin: 3%; width: 31.5vw; border-radius: 20px; background-color: #555; padding: 0px 1%; border: 5px #ececec dashed; justify-self: center">
                    <a href="dashboard.jsp"><h1>CARDLORD</h1></a>
                </div>
            <button class="chatbot-toggler" style="width: 80px; height: 80px">
                <span class="material-symbols-rounded">mode_comment</span>
                <span class="material-symbols-outlined">close</span>
            </button>
            <div class="chatbot">
                <header>
                    <h2>Cardlord AI</h2>
                    <span class="close-btn material-symbols-outlined">close</span>
                </header>
                <ul class="chatbox">
                    <li class="chat incoming">
                        <span class="material-symbols-outlined">smart_toy</span>
                        <p>Hi, I'm Cardlord AI ?<br />How can I help you today?</p>
                    </li>
                </ul>
                <div class="chat-input">
                    <textarea placeholder="Enter a message..." spellcheck="false" required></textarea>
                    <span id="send-btn" class="material-symbols-rounded">send</span>
                </div>
            </div>

            <script src="Javascript/script.js" defer></script>

        </body>
    </html>
