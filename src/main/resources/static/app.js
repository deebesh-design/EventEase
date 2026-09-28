// =====================================================
// EVENTEASE APP.JS
// =====================================================

// Backend API base URL
const API_BASE = "";


// =====================================================
// CURRENT USER
// =====================================================

let currentStudent = null;
let currentOrganizer = null;


// =====================================================
// SECTION NAVIGATION
// =====================================================

function showSection(sectionId) {

    const sections =
        document.querySelectorAll(".page-section");

    sections.forEach(section => {
        section.classList.remove("active-section");
    });

    const selectedSection =
        document.getElementById(sectionId);

    if (selectedSection) {
        selectedSection.classList.add("active-section");
    }

    window.scrollTo(0, 0);
}


// =====================================================
// STUDENT REGISTRATION
// =====================================================

document.getElementById("studentRegisterForm")
    .addEventListener("submit", async function (event) {

        event.preventDefault();

        const name =
            document.getElementById("studentName").value;

        const email =
            document.getElementById("studentEmail").value;

        const password =
            document.getElementById("studentPassword").value;

        const message =
            document.getElementById("studentRegisterMessage");

        try {

            const response = await fetch(
                API_BASE + "/students",
                {
                    method: "POST",

                    headers: {
                        "Content-Type": "application/json"
                    },

                    body: JSON.stringify({
                        name: name,
                        email: email,
                        password: password
                    })
                }
            );

            const data = await response.json();

            if (!response.ok) {

                message.textContent =
                    data.error || "Registration failed";

                return;
            }

            message.textContent =
                "Student account created successfully!";

            document.getElementById(
                "studentRegisterForm"
            ).reset();

            setTimeout(() => {

                showSection("studentLoginSection");

            }, 1000);

        } catch (error) {

            message.textContent =
                "Unable to connect to server";
        }

    });


// =====================================================
// STUDENT LOGIN
// =====================================================

document.getElementById("studentLoginForm")
    .addEventListener("submit", async function (event) {

        event.preventDefault();

        const email =
            document.getElementById(
                "studentLoginEmail"
            ).value;

        const password =
            document.getElementById(
                "studentLoginPassword"
            ).value;

        const message =
            document.getElementById(
                "studentLoginMessage"
            );

        try {

            const response = await fetch(
                API_BASE +
                "/students/login?email=" +
                encodeURIComponent(email) +
                "&password=" +
                encodeURIComponent(password),
                {
                    method: "POST"
                }
            );

            const data = await response.json();

            if (!response.ok) {

                message.textContent =
                    data.error || "Login failed";

                return;
            }

            currentStudent = data;

            document.getElementById(
                "studentWelcome"
            ).textContent =
                "Welcome, " + currentStudent.name;

            message.textContent =
                "Login successful";

            document.getElementById(
                "studentLoginForm"
            ).reset();

            showSection("studentDashboardSection");

            loadEvents();

            loadStudentRegistrations();

        } catch (error) {

            message.textContent =
                "Unable to connect to server";
        }

    });


// =====================================================
// STUDENT LOGOUT
// =====================================================

function studentLogout() {

    currentStudent = null;

    showSection("homeSection");
}


// =====================================================
// LOAD ALL EVENTS
// =====================================================

async function loadEvents() {

    const container =
        document.getElementById(
            "eventsContainer"
        );

    container.innerHTML =
        "<p>Loading events...</p>";

    try {

        const response =
            await fetch(
                API_BASE + "/events"
            );

        const events =
            await response.json();

        if (!response.ok) {

            container.innerHTML =
                "<p>Unable to load events.</p>";

            return;
        }

        if (events.length === 0) {

            container.innerHTML =
                "<p>No events available.</p>";

            return;
        }

        container.innerHTML = "";

        for (const event of events) {

            const registeredCount =
                await getRegisteredCount(event.id);

            const availableSeats =
                event.capacity -
                registeredCount;

            let statusText;

            if (availableSeats <= 0) {

                statusText =
                    '<span class="status-full">FULL</span>';

            } else {

                statusText =
                    '<span class="status-available">AVAILABLE</span>';
            }

            const card =
                document.createElement("div");

            card.className = "event-card";

            card.innerHTML = `

                <h4>${event.title}</h4>

                <p>
                    <strong>Date:</strong>
                    ${event.date}
                </p>

                <p>
                    <strong>Venue:</strong>
                    ${event.venue}
                </p>

                <p>
                    <strong>Capacity:</strong>
                    ${event.capacity}
                </p>

                <p>
                    <strong>Registered:</strong>
                    ${registeredCount}
                </p>

                <p>
                    <strong>Available Seats:</strong>
                    ${Math.max(availableSeats, 0)}
                </p>

                <p>
                    <strong>Status:</strong>
                    ${statusText}
                </p>

                <div class="event-actions">

                    ${
                availableSeats > 0
                    ?
                    `
                        <button
                            class="register-button"
                            onclick="registerForEvent(${event.id})">

                            Register

                        </button>
                        `
                    :
                    `
                        <button
                            class="cancel-button"
                            disabled>

                            Event Full

                        </button>
                        `
            }

                </div>
            `;

            container.appendChild(card);
        }

    } catch (error) {

        container.innerHTML =
            "<p>Unable to connect to server.</p>";
    }
}


// =====================================================
// GET REGISTERED COUNT
// =====================================================

async function getRegisteredCount(eventId) {

    try {

        const response =
            await fetch(
                API_BASE +
                "/events/" +
                eventId +
                "/registered-count"
            );

        if (!response.ok) {
            return 0;
        }

        return await response.json();

    } catch (error) {

        return 0;
    }
}


// =====================================================
// STUDENT REGISTER FOR EVENT
// =====================================================

async function registerForEvent(eventId) {

    if (!currentStudent) {

        alert("Please login as a student first.");

        return;
    }

    try {

        const response =
            await fetch(
                API_BASE +
                "/registrations?studentId=" +
                currentStudent.id +
                "&eventId=" +
                eventId,
                {
                    method: "POST"
                }
            );

        const data =
            await response.json();

        if (!response.ok) {

            alert(
                data.error ||
                "Registration failed"
            );

            return;
        }

        alert(
            "Successfully registered for the event!"
        );

        loadEvents();

        loadStudentRegistrations();

    } catch (error) {

        alert(
            "Unable to connect to server"
        );
    }
}


// =====================================================
// LOAD STUDENT REGISTRATIONS
// =====================================================

async function loadStudentRegistrations() {

    const container =
        document.getElementById(
            "studentRegistrationsContainer"
        );

    if (!currentStudent) {

        container.innerHTML =
            "<p>Please login first.</p>";

        return;
    }

    try {

        const response =
            await fetch(
                API_BASE + "/registrations"
            );

        const registrations =
            await response.json();

        if (!response.ok) {

            container.innerHTML =
                "<p>Unable to load registrations.</p>";

            return;
        }

        const myRegistrations =
            registrations.filter(
                registration =>
                    registration.student &&
                    registration.student.id ===
                    currentStudent.id
            );

        if (myRegistrations.length === 0) {

            container.innerHTML =
                "<p>You have not registered for any event.</p>";

            return;
        }

        container.innerHTML = "";

        for (const registration of myRegistrations) {

            const event =
                registration.event;

            const card =
                document.createElement("div");

            card.className = "event-card";

            card.innerHTML = `

                <h4>${event.title}</h4>

                <p>
                    <strong>Date:</strong>
                    ${event.date}
                </p>

                <p>
                    <strong>Venue:</strong>
                    ${event.venue}
                </p>

                <div class="event-actions">

                    <button
                        class="cancel-button"
                        onclick="cancelRegistration(
                            ${registration.id}
                        )">

                        Cancel Registration

                    </button>

                </div>

            `;

            container.appendChild(card);
        }

    } catch (error) {

        container.innerHTML =
            "<p>Unable to connect to server.</p>";
    }
}


// =====================================================
// CANCEL STUDENT REGISTRATION
// =====================================================

async function cancelRegistration(
    registrationId
) {

    const confirmCancel =
        confirm(
            "Are you sure you want to cancel this registration?"
        );

    if (!confirmCancel) {
        return;
    }

    try {

        const response =
            await fetch(
                API_BASE +
                "/registrations/" +
                registrationId,
                {
                    method: "DELETE"
                }
            );

        const data =
            await response.json();

        if (!response.ok) {

            alert(
                data.error ||
                "Unable to cancel registration"
            );

            return;
        }

        alert(
            "Registration cancelled successfully"
        );

        loadStudentRegistrations();

        loadEvents();

    } catch (error) {

        alert(
            "Unable to connect to server"
        );
    }
}


// =====================================================
// ORGANIZER REGISTRATION
// =====================================================

document.getElementById("organizerRegisterForm")
    .addEventListener("submit", async function (event) {

        event.preventDefault();

        const name =
            document.getElementById(
                "organizerName"
            ).value;

        const email =
            document.getElementById(
                "organizerEmail"
            ).value;

        const password =
            document.getElementById(
                "organizerPassword"
            ).value;

        const message =
            document.getElementById(
                "organizerRegisterMessage"
            );

        try {

            const response =
                await fetch(
                    API_BASE + "/organizers",
                    {
                        method: "POST",

                        headers: {
                            "Content-Type":
                                "application/json"
                        },

                        body: JSON.stringify({
                            name: name,
                            email: email,
                            password: password
                        })
                    }
                );

            const data =
                await response.json();

            if (!response.ok) {

                message.textContent =
                    data.error ||
                    "Registration failed";

                return;
            }

            message.textContent =
                "Organizer account created successfully!";

            document.getElementById(
                "organizerRegisterForm"
            ).reset();

            setTimeout(() => {

                showSection(
                    "organizerLoginSection"
                );

            }, 1000);

        } catch (error) {

            message.textContent =
                "Unable to connect to server";
        }

    });


// =====================================================
// ORGANIZER LOGIN
// =====================================================

document.getElementById("organizerLoginForm")
    .addEventListener("submit", async function (event) {

        event.preventDefault();

        const email =
            document.getElementById(
                "organizerLoginEmail"
            ).value;

        const password =
            document.getElementById(
                "organizerLoginPassword"
            ).value;

        const message =
            document.getElementById(
                "organizerLoginMessage"
            );

        try {

            const response =
                await fetch(
                    API_BASE +
                    "/organizers/login?email=" +
                    encodeURIComponent(email) +
                    "&password=" +
                    encodeURIComponent(password),
                    {
                        method: "POST"
                    }
                );

            const data =
                await response.json();

            if (!response.ok) {

                message.textContent =
                    data.error ||
                    "Login failed";

                return;
            }

            currentOrganizer = data;

            document.getElementById(
                "organizerWelcome"
            ).textContent =
                "Welcome, " +
                currentOrganizer.name;

            message.textContent =
                "Login successful";

            document.getElementById(
                "organizerLoginForm"
            ).reset();

            showSection(
                "organizerDashboardSection"
            );

            loadOrganizerEvents();

        } catch (error) {

            message.textContent =
                "Unable to connect to server";
        }

    });


// =====================================================
// ORGANIZER LOGOUT
// =====================================================

function organizerLogout() {

    currentOrganizer = null;

    showSection("homeSection");
}


// =====================================================
// CREATE EVENT
// =====================================================

document.getElementById("createEventForm")
    .addEventListener("submit", async function (event) {

        event.preventDefault();

        if (!currentOrganizer) {

            alert(
                "Please login as an organizer first."
            );

            return;
        }

        const title =
            document.getElementById(
                "eventTitle"
            ).value;

        const date =
            document.getElementById(
                "eventDate"
            ).value;

        const venue =
            document.getElementById(
                "eventVenue"
            ).value;

        const capacity =
            document.getElementById(
                "eventCapacity"
            ).value;

        const message =
            document.getElementById(
                "createEventMessage"
            );

        try {

            const response =
                await fetch(
                    API_BASE +
                    "/events?organizerId=" +
                    currentOrganizer.id,
                    {
                        method: "POST",

                        headers: {
                            "Content-Type":
                                "application/json"
                        },

                        body: JSON.stringify({

                            title: title,

                            date: date,

                            venue: venue,

                            capacity:
                                Number(capacity)

                        })
                    }
                );

            const data =
                await response.json();

            if (!response.ok) {

                message.textContent =
                    data.error ||
                    "Unable to create event";

                return;
            }

            message.textContent =
                "Event created successfully!";

            document.getElementById(
                "createEventForm"
            ).reset();

            loadOrganizerEvents();

        } catch (error) {

            message.textContent =
                "Unable to connect to server";
        }

    });


// =====================================================
// LOAD ORGANIZER EVENTS
// =====================================================

async function loadOrganizerEvents() {

    const container =
        document.getElementById(
            "organizerEventsContainer"
        );

    if (!currentOrganizer) {

        container.innerHTML =
            "<p>Please login first.</p>";

        return;
    }

    container.innerHTML =
        "<p>Loading events...</p>";

    try {

        const response =
            await fetch(
                API_BASE +
                "/events/organizer/" +
                currentOrganizer.id
            );

        const events =
            await response.json();

        if (!response.ok) {

            container.innerHTML =
                "<p>Unable to load events.</p>";

            return;
        }

        if (events.length === 0) {

            container.innerHTML =
                "<p>You have not created any events.</p>";

            return;
        }

        container.innerHTML = "";

        for (const event of events) {

            const registeredCount =
                await getRegisteredCount(event.id);

            const availableSeats =
                event.capacity -
                registeredCount;

            const card =
                document.createElement("div");

            card.className =
                "event-card";

            card.innerHTML = `

                <h4>${event.title}</h4>

                <p>
                    <strong>Date:</strong>
                    ${event.date}
                </p>

                <p>
                    <strong>Venue:</strong>
                    ${event.venue}
                </p>

                <p>
                    <strong>Capacity:</strong>
                    ${event.capacity}
                </p>

                <p>
                    <strong>Registered:</strong>
                    ${registeredCount}
                </p>

                <p>
                    <strong>Available Seats:</strong>
                    ${Math.max(
                availableSeats,
                0
            )}
                </p>

                <div class="event-actions">

                    <button
                        class="edit-button"
                        onclick="openEditEvent(
                            ${event.id}
                        )">

                        Edit

                    </button>

                    <button
                        class="delete-button"
                        onclick="deleteEvent(
                            ${event.id}
                        )">

                        Delete

                    </button>

                    <button
                        class="view-button"
                        onclick="viewEventRegistrations(
                            ${event.id}
                        )">

                        View Registrations

                    </button>

                </div>

            `;

            container.appendChild(card);
        }

    } catch (error) {

        container.innerHTML =
            "<p>Unable to connect to server.</p>";
    }
}


// =====================================================
// OPEN EDIT EVENT
// =====================================================

async function openEditEvent(eventId) {

    try {

        const response =
            await fetch(
                API_BASE +
                "/events/" +
                eventId
            );

        const event =
            await response.json();

        if (!response.ok) {

            alert(
                event.error ||
                "Unable to load event"
            );

            return;
        }

        document.getElementById(
            "editEventId"
        ).value = event.id;

        document.getElementById(
            "editEventTitle"
        ).value = event.title;

        document.getElementById(
            "editEventDate"
        ).value = event.date;

        document.getElementById(
            "editEventVenue"
        ).value = event.venue;

        document.getElementById(
            "editEventCapacity"
        ).value = event.capacity;

        showSection(
            "editEventSection"
        );

    } catch (error) {

        alert(
            "Unable to connect to server"
        );
    }
}


// =====================================================
// UPDATE EVENT
// =====================================================

document.getElementById("editEventForm")
    .addEventListener("submit", async function (event) {

        event.preventDefault();

        if (!currentOrganizer) {

            alert(
                "Please login as organizer."
            );

            return;
        }

        const eventId =
            document.getElementById(
                "editEventId"
            ).value;

        const title =
            document.getElementById(
                "editEventTitle"
            ).value;

        const date =
            document.getElementById(
                "editEventDate"
            ).value;

        const venue =
            document.getElementById(
                "editEventVenue"
            ).value;

        const capacity =
            document.getElementById(
                "editEventCapacity"
            ).value;

        const message =
            document.getElementById(
                "editEventMessage"
            );

        try {

            const response =
                await fetch(
                    API_BASE +
                    "/events/" +
                    eventId +
                    "?organizerId=" +
                    currentOrganizer.id,
                    {
                        method: "PUT",

                        headers: {
                            "Content-Type":
                                "application/json"
                        },

                        body: JSON.stringify({

                            title: title,

                            date: date,

                            venue: venue,

                            capacity:
                                Number(capacity)

                        })
                    }
                );

            const data =
                await response.json();

            if (!response.ok) {

                message.textContent =
                    data.error ||
                    "Unable to update event";

                return;
            }

            message.textContent =
                "Event updated successfully!";

            setTimeout(() => {

                showSection(
                    "organizerDashboardSection"
                );

                loadOrganizerEvents();

            }, 800);

        } catch (error) {

            message.textContent =
                "Unable to connect to server";
        }

    });


// =====================================================
// DELETE EVENT
// =====================================================

async function deleteEvent(eventId) {

    if (!currentOrganizer) {

        alert(
            "Please login as organizer."
        );

        return;
    }

    const confirmDelete =
        confirm(
            "Are you sure you want to delete this event?"
        );

    if (!confirmDelete) {
        return;
    }

    try {

        const response =
            await fetch(
                API_BASE +
                "/events/" +
                eventId +
                "?organizerId=" +
                currentOrganizer.id,
                {
                    method: "DELETE"
                }
            );

        const data =
            await response.json();

        if (!response.ok) {

            alert(
                data.error ||
                "Unable to delete event"
            );

            return;
        }

        alert(
            "Event deleted successfully"
        );

        loadOrganizerEvents();

    } catch (error) {

        alert(
            "Unable to connect to server"
        );
    }
}


// =====================================================
// VIEW EVENT REGISTRATIONS
// =====================================================

async function viewEventRegistrations(
    eventId
) {

    const container =
        document.getElementById(
            "eventRegistrationsContainer"
        );

    container.innerHTML =
        "<p>Loading registrations...</p>";

    try {

        const response =
            await fetch(
                API_BASE +
                "/registrations/event/" +
                eventId
            );

        const registrations =
            await response.json();

        if (!response.ok) {

            container.innerHTML =
                "<p>Unable to load registrations.</p>";

            return;
        }

        if (registrations.length === 0) {

            container.innerHTML =
                "<p>No students registered for this event.</p>";

            return;
        }

        let html = `

            <table>

                <thead>

                    <tr>

                        <th>Registration ID</th>

                        <th>Student Name</th>

                        <th>Email</th>

                    </tr>

                </thead>

                <tbody>

        `;

        registrations.forEach(
            registration => {

                html += `

                    <tr>

                        <td>
                            ${registration.id}
                        </td>

                        <td>
                            ${registration.student.name}
                        </td>

                        <td>
                            ${registration.student.email}
                        </td>

                    </tr>

                `;
            }
        );

        html += `

                </tbody>

            </table>

        `;

        container.innerHTML = html;

    } catch (error) {

        container.innerHTML =
            "<p>Unable to connect to server.</p>";
    }
}