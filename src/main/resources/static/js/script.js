$(document).ready(function() {
    loadUsers();
    setupEventListeners();

    function setupEventListeners() {
        $("#userForm").on("submit", function(event) {
            event.preventDefault();
            submitUserForm();
        });

        $("#refreshBtn").on("click", function() {
            loadUsers();
        });
    }

    function loadUsers() {
        $("#loadingUsers").removeClass("d-none");
        $("#noUsers").addClass("d-none");
        
        $.ajax({
            url: "/api/users",
            type: "GET",
            dataType: "json",
            success: function(response) {
                $("#loadingUsers").addClass("d-none");

                $("#userTableBody").empty();
                
                if (response && response.length > 0) {
                    populateUserTable(response);
                } else {
                    $("#noUsers").removeClass("d-none");
                }
            },
            error: function(xhr, status, error) {
                $("#loadingUsers").addClass("d-none");

                showAlert("Error loading users: " + (xhr.responseJSON?.error || error), "danger");
            }
        });
    }

    function populateUserTable(users) {
        users.forEach(function(user) {
            const row = `
                <tr>
                    <td>${user.id}</td>
                    <td>${escapeHtml(user.name)}</td>
                    <td>${escapeHtml(user.email)}</td>
                    <td>${escapeHtml(user.phone || "")}</td>
                    <td>${formatDate(user.createdAt)}</td>
                </tr>
            `;
            $("#userTableBody").append(row);
        });
    }
    
    // Submit the user form via AJAX
    function submitUserForm() {
        const userData = {
            name: $("#name").val().trim(),
            email: $("#email").val().trim(),
            phone: $("#phone").val().trim()
        };

        if (!userData.name || !userData.email) {
            showAlert("Name and email are required fields.", "warning");
            return;
        }

        const submitBtn = $("#userForm button[type='submit']");
        const originalText = submitBtn.text();
        submitBtn.prop("disabled", true).html('<span class="spinner-border spinner-border-sm" role="status" aria-hidden="true"></span> Saving...');
        
        $.ajax({
            url: "/api/users",
            type: "POST",
            contentType: "application/json",
            data: JSON.stringify(userData),
            dataType: "json",
            success: function(response) {
                $("#userForm")[0].reset();
                submitBtn.prop("disabled", false).text(originalText);

                showAlert("User added successfully!", "success");

                loadUsers();
            },
            error: function(xhr, status, error) {
                submitBtn.prop("disabled", false).text(originalText);

                const errorMsg = xhr.responseJSON?.error || "Error adding user. Please try again.";
                showAlert(errorMsg, "danger");
            }
        });
    }

    function showAlert(message, type) {
        const alertEl = $("#alertMessage");
        alertEl.text(message)
               .removeClass("d-none alert-success alert-warning alert-danger")
               .addClass("alert-" + type)
               .show();

        setTimeout(function() {
            alertEl.addClass("d-none");
        }, 5000);
    }

    function formatDate(dateString) {
        const date = new Date(dateString);
        return date.toLocaleString();
    }

    function escapeHtml(str) {
        if (!str) return "";
        return str
            .replace(/&/g, "&amp;")
            .replace(/</g, "&lt;")
            .replace(/>/g, "&gt;")
            .replace(/"/g, "&quot;")
            .replace(/'/g, "&#039;");
    }
}); 