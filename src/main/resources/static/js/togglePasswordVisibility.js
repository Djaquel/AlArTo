    function togglePasswordVisibility() {
        const passwordInput = document.getElementById("password");
        const passwordToggleImage = document.getElementById("passwordToggleImage");
                
        if (passwordInput.type === "password") {
            passwordInput.type = "text";
            passwordToggleImage.src = "/images/hide.png";
        } else {
            passwordInput.type = "password";
            passwordToggleImage.src = "/images/show.png"; 
        }
    }