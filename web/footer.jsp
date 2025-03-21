<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Footer</title>
        <style>
            :root {
                --primary-bg: #545454;
                --secondary-bg: #ececec;
                --text-color: #EAEAEA;
                --accent-color: #ececec;
            }

            footer {
                background: var(--primary-bg);
                color: var(--text-color);
                padding: 50px 20px;
                display: flex;
                flex-direction: column;
                align-items: center;
                text-align: center;
                padding-top: 100px;
            }

            .footer-container {
                width: 90%;
                max-width: 1200px;
                display: flex;
                flex-wrap: wrap;
                justify-content: space-around;
                gap: 20px;
            }

            .footer-column {
                flex: 1;
                min-width: 200px;
                text-align: left;
            }

            .footer-column h3 {
                font-size: 1.2rem;
                margin-bottom: 15px;
                color: var(--accent-color);
                font-weight: bolder;
            }

            .footer-column ul {
                list-style: none;
                padding: 0;
            }

            .footer-column li {
                margin-bottom: 8px;
            }

            .footer-column a {
                color: var(--text-color);
                text-decoration: none;
                transition: 0.3s;
            }

            .footer-column a:hover {
                color: var(--accent-color);
                text-decoration: underline;
            }

            .copyright {
                margin-top: 30px;
                font-size: 0.9rem;
                border-top: 1px solid var(--secondary-bg);
                padding-top: 15px;
                width: 100%;
                max-width: 1200px;
            }
        </style>
    </head>
    <body>
        <footer>
            <div class="footer-container">
                <div class="footer-column">
                    <h3>Help</h3>
                    <ul>
                        <li><a href="contact.html">Contact Support</a></li>
                        <li><a href="faqs.html">FAQs</a></li>
                    </ul>
                </div>

                <div class="footer-column">
                    <h3>Information</h3>
                    <ul>
                        <li><a href="index.jsp">About Us</a></li>
                        <li><a href="terms.html">Terms & Policies</a></li>
                    </ul>
                </div>

                <div class="footer-column">
                    <h3>Account</h3>
                    <ul>
                        <li><a href="QR.jsp">Upgrade Account</a></li>
                        <li><a href="QR.jsp">Become CARDLORD</a></li>
                    </ul>
                </div>
            </div>

            <div class="copyright">
                <p>&copy; PRJ301_Project Group 1 - FPT University</p>
            </div>
        </footer>
    </body>
</html>
