// =====================================================
// EVENTEASE FRONTEND
// =====================================================

const API_BASE = "";

let currentStudent = null;
let currentOrganizer = null;


// =====================================================
// SECTION NAVIGATION
// =====================================================

function showSection(sectionId) {

    const sections =
        document.querySelectorAll(".page-section");

    sections.forEach(function(section) {

        section.classList.remove("active-section");

    });

    const selectedSection =
        document.getElementById(sectionId);

    if (selectedSection) {

        selectedSection.classList.add("active-section");

    }

}


// =====================================================
// STUDENT REGISTRATION
// =====================================================

async function registerStudent() {

    const name =
        document.getElementById("studentName").value.trim();

    const email =
        document.getElementById("studentEmail").value.trim();

    const password =
        document.getElementById("studentPassword").value.trim();


    // Basic validation

    if (name === "") {

        alert("Please enter student name");

        return;
    }

    if (email === "") {

        alert("Please enter student email");

        return;
    }

    if (password === "") {

        alert("Please enter student password");

        return;
    }


    const studentData = {

        name: name,

        email: email,

        password: password

    };


    try {

        const response =
            await fetch(API_BASE + "/students", {

                method: "POST",

                headers: {

                    "Content-Type":
                        "application/json"

                },

                body:
                    JSON.stringify(studentData)

            });


        const data =
            await response.json();


        if (!response.ok) {

            alert(
                data.error ||
                "Student registration failed"
            );

            return;
        }


        alert(
            "Student registered successfully!"
        );


        document.getElementById(
            "studentName"
        ).value = "";

        document.getElementById(
            "studentEmail"
        ).value = "";

        document.getElementById(
            "studentPassword"
        ).value = "";


        showSection("studentLogin");


    } catch (error) {

        console.error(error);

        alert(
            "Unable to connect to the server"
        );

    }

}


// =====================================================
// STUDENT LOGIN
// =====================================================

async function studentLogin() {

    const email =
        document
            .getElementById("studentLoginEmail")
            .value
            .trim();

    const password =
        document
            .getElementById("studentLoginPassword")
            .value
            .trim();


    if (email === "") {

        alert("Please enter email");

        return;
    }


    if (password === "") {

        alert("Please enter password");

        return;
    }


    try {

        const url =
            API_BASE +
            "/students/login" +
            "?email=" +
            encodeURIComponent(email) +
            "&password=" +
            encodeURIComponent(password);


        const response =
            await fetch(url, {

                method: "POST"

            });


        const data =
            await response.json();


        if (!response.ok) {

            alert(
                data.error ||
                "Student login failed"
            );

            return;
        }


        currentStudent = data;


        document.getElementById(
            "studentWelcome"
        ).innerText =
            "Welcome, " + currentStudent.name;


        alert("Student login successful!");


        showSection("studentDashboard");


        loadStudentEvents();

        loadStudentRegistrations();


    } catch (error) {

        console.error(error);

        alert(
            "Unable to connect to the server"
        );

    }

}


// =====================================================
// STUDENT LOGOUT
// =====================================================

function studentLogout() {

    currentStudent = null;

    alert("Student logged out");

    showSection("home");

}


// =====================================================
// LOAD ALL EVENTS
// =====================================================

async function loadEvents() {

    try {

        const response =
            await fetch(
                API_BASE + "/events"
            );


        const events =
            await response.json();


        if (!response.ok) {

            alert(
                events.error ||
                "Unable to load events"
            );

            return;
        }


        displayEvents(
            events,
            "eventsContainer",
            false
        );


    } catch (error) {

        console.error(error);

        alert(
            "Unable to connect to the server"
        );

    }

}


// =====================================================
// LOAD STUDENT EVENTS
// =====================================================

async function loadStudentEvents() {

    try {

        const response =
            await fetch(
                API_BASE + "/events"
            );


        const events =
            await response.json();


        if (!response.ok) {

            alert(
                events.error ||
                "Unable to load events"
            );

            return;
        }


        displayEvents(
            events,
            "studentEventsContainer",
            true
        );


    } catch (error) {

        console.error(error);

        alert(
            "Unable to connect to the server"
        );

    }

}


// =====================================================
// DISPLAY EVENTS
// =====================================================

async function displayEvents(
    events,
    containerId,
    allowRegistration
) {

    const container =
        document.getElementById(containerId);


    if (!events || events.length === 0) {

        container.innerHTML =
            "<p>No events available.</p>";

        return;
    }


    container.innerHTML = "";


    for (const event of events) {

        let registeredCount = 0;

        let availableSeats = 0;


        try {

            const countResponse =
                await fetch(
                    API_BASE +
                    "/events/" +
                    event.id +
                    "/registered-count"
                );


            registeredCount =
                await countResponse.json();


            const seatsResponse =
                await fetch(
                    API_BASE +
                    "/events/" +
                    event.id +
                    "/available-seats"
                );


            availableSeats =
                await seatsResponse.json();


        } catch (error) {

            console.error(error);

        }


        const card =
            document.createElement("div");


        card.className =
            "event-card";


        let statusHTML;


        if (availableSeats > 0) {

            statusHTML =
                `<p class="status-available">
                    Available
                </p>`;

        } else {

            statusHTML =
                `<p class="status-full">
                    FULL
                </p>`;

        }


        let buttonHTML = "";


        if (allowRegistration) {

            if (availableSeats > 0) {

                buttonHTML =
                    `
                    <button
                        class="primary-btn"
                        onclick="registerForEvent(${event.id})">

                        Register for Event

                    </button>
                    `;

            } else {

                buttonHTML =
                    `
                    <button
                        class="danger-btn"
                        disabled>

                        Event Full

                    </button>
                    `;

            }

        }


        card.innerHTML = `

            <h3>
                ${event.title}
            </h3>

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
                ${availableSeats}
            </p>

            ${statusHTML}

            ${buttonHTML}

        `;


        container.appendChild(card);

    }

}


// =====================================================
// REGISTER STUDENT FOR EVENT
// =====================================================

async function registerForEvent(eventId) {

    if (!currentStudent) {

        alert(
            "Please login as a student first"
        );

        showSection("studentLogin");

        return;
    }


    try {

        const url =
            API_BASE +
            "/registrations" +
            "?studentId=" +
            currentStudent.id +
            "&eventId=" +
            eventId;


        const response =
            await fetch(url, {

                method: "POST"

            });


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


        loadStudentEvents();

        loadStudentRegistrations();


    } catch (error) {

        console.error(error);

        alert(
            "Unable to connect to the server"
        );

    }

}


// =====================================================
// LOAD STUDENT REGISTRATIONS
// =====================================================

async function loadStudentRegistrations() {

    if (!currentStudent) {

        return;
    }


    try {

        const response =
            await fetch(
                API_BASE +
                "/registrations"
            );


        const registrations =
            await response.json();


        if (!response.ok) {

            alert(
                registrations.error ||
                "Unable to load registrations"
            );

            return;
        }


        const myRegistrations =
            registrations.filter(
                function(registration) {

                    return registration.student.id
                        === currentStudent.id;

                }
            );


        const container =
            document.getElementById(
                "studentRegistrationsContainer"
            );


        if (myRegistrations.length === 0) {

            container.innerHTML =
                "<p>You have no registrations.</p>";

            return;
        }


        let html = `

            <table>

                <tr>

                    <th>
                        Event
                    </th>

                    <th>
                        Date
                    </th>

                    <th>
                        Venue
                    </th>

                    <th>
                        Action
                    </th>

                </tr>

        `;


        myRegistrations.forEach(
            function(registration) {

                html += `

                    <tr>

                        <td>
                            ${registration.event.title}
                        </td>

                        <td>
                            ${registration.event.date}
                        </td>

                        <td>
                            ${registration.event.venue}
                        </td>

                        <td>

                            <button
                                class="danger-btn"
                                onclick="cancelRegistration(
                                    ${registration.id}
                                )">

                                Cancel

                            </button>

                        </td>

                    </tr>

                `;

            }
        );


        html += "</table>";


        container.innerHTML = html;


    } catch (error) {

        console.error(error);

        alert(
            "Unable to connect to the server"
        );

    }

}


// =====================================================
// CANCEL REGISTRATION
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
            await response.json().catch(
                function() {
                    return null;
                }
            );


        if (!response.ok) {

            alert(
                data && data.error
                    ? data.error
                    : "Cancellation failed"
            );

            return;
        }


        alert(
            "Registration cancelled successfully"
        );


        loadStudentRegistrations();

        loadStudentEvents();


    } catch (error) {

        console.error(error);

        alert(
            "Unable to connect to the server"
        );

    }

}


// =====================================================
// ORGANIZER REGISTRATION
// =====================================================

async function registerOrganizer() {

    const name =
        document
            .getElementById("organizerName")
            .value
            .trim();

    const email =
        document
            .getElementById("organizerEmail")
            .value
            .trim();

    const password =
        document
            .getElementById("organizerPassword")
            .value
            .trim();


    if (name === "") {

        alert("Please enter organizer name");

        return;
    }


    if (email === "") {

        alert("Please enter organizer email");

        return;
    }


    if (password === "") {

        alert("Please enter organizer password");

        return;
    }


    const organizerData = {

        name: name,

        email: email,

        password: password

    };


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

                    body:
                        JSON.stringify(
                            organizerData
                        )

                }
            );


        const data =
            await response.json();


        if (!response.ok) {

            alert(
                data.error ||
                "Organizer registration failed"
            );

            return;
        }


        alert(
            "Organizer registered successfully!"
        );


        document.getElementById(
            "organizerName"
        ).value = "";

        document.getElementById(
            "organizerEmail"
        ).value = "";

        document.getElementById(
            "organizerPassword"
        ).value = "";


        showSection("organizerLogin");


    } catch (error) {

        console.error(error);

        alert(
            "Unable to connect to the server"
        );

    }

}


// =====================================================
// ORGANIZER LOGIN
// =====================================================

async function organizerLogin() {

    const email =
        document
            .getElementById("organizerLoginEmail")
            .value
            .trim();

    const password =
        document
            .getElementById("organizerLoginPassword")
            .value
            .trim();


    if (email === "") {

        alert("Please enter email");

        return;
    }


    if (password === "") {

        alert("Please enter password");

        return;
    }


    try {

        const url =
            API_BASE +
            "/organizers/login" +
            "?email=" +
            encodeURIComponent(email) +
            "&password=" +
            encodeURIComponent(password);


        const response =
            await fetch(url, {

                method: "POST"

            });


        const data =
            await response.json();


        if (!response.ok) {

            alert(
                data.error ||
                "Organizer login failed"
            );

            return;
        }


        currentOrganizer = data;


        document.getElementById(
            "organizerWelcome"
        ).innerText =
            "Welcome, " +
            currentOrganizer.name;


        alert(
            "Organizer login successful!"
        );


        showSection("organizerDashboard");


        loadOrganizerEvents();


    } catch (error) {

        console.error(error);

        alert(
            "Unable to connect to the server"
        );

    }

}


// =====================================================
// ORGANIZER LOGOUT
// =====================================================

function organizerLogout() {

    currentOrganizer = null;

    alert("Organizer logged out");

    showSection("home");

}


// =====================================================
// CREATE EVENT
// =====================================================

async function createEvent() {

    if (!currentOrganizer) {

        alert(
            "Please login as an organizer first"
        );

        showSection("organizerLogin");

        return;
    }


    const title =
        document
            .getElementById("eventTitle")
            .value
            .trim();

    const date =
        document
            .getElementById("eventDate")
            .value;

    const venue =
        document
            .getElementById("eventVenue")
            .value
            .trim();

    const capacity =
        Number(
            document
                .getElementById("eventCapacity")
                .value
        );


    if (title === "") {

        alert("Please enter event title");

        return;
    }


    if (date === "") {

        alert("Please select event date");

        return;
    }


    if (venue === "") {

        alert("Please enter venue");

        return;
    }


    if (capacity <= 0) {

        alert(
            "Capacity must be greater than 0"
        );

        return;
    }


    const eventData = {

        title: title,

        date: date,

        venue: venue,

        capacity: capacity

    };


    try {

        const url =
            API_BASE +
            "/events?organizerId=" +
            currentOrganizer.id;


        const response =
            await fetch(url, {

                method: "POST",

                headers: {

                    "Content-Type":
                        "application/json"

                },

                body:
                    JSON.stringify(eventData)

            });


        const data =
            await response.json();


        if (!response.ok) {

            alert(
                data.error ||
                "Event creation failed"
            );

            return;
        }


        alert(
            "Event created successfully!"
        );


        document.getElementById(
            "eventTitle"
        ).value = "";

        document.getElementById(
            "eventDate"
        ).value = "";

        document.getElementById(
            "eventVenue"
        ).value = "";

        document.getElementById(
            "eventCapacity"
        ).value = "";


        loadOrganizerEvents();

        loadEvents();


    } catch (error) {

        console.error(error);

        alert(
            "Unable to connect to the server"
        );

    }

}


// =====================================================
// LOAD ORGANIZER EVENTS
// =====================================================

async function loadOrganizerEvents() {

    if (!currentOrganizer) {

        return;
    }


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

            alert(
                events.error ||
                "Unable to load organizer events"
            );

            return;
        }


        const container =
            document.getElementById(
                "organizerEventsContainer"
            );


        if (events.length === 0) {

            container.innerHTML =
                "<p>You have not created any events.</p>";

            return;
        }


        container.innerHTML = "";


        for (const event of events) {

            let registeredCount = 0;

            let availableSeats = 0;


            try {

                const countResponse =
                    await fetch(
                        API_BASE +
                        "/events/" +
                        event.id +
                        "/registered-count"
                    );


                registeredCount =
                    await countResponse.json();


                const seatsResponse =
                    await fetch(
                        API_BASE +
                        "/events/" +
                        event.id +
                        "/available-seats"
                    );


                availableSeats =
                    await seatsResponse.json();


            } catch (error) {

                console.error(error);

            }


            const card =
                document.createElement("div");


            card.className =
                "event-card";


            card.innerHTML = `

                <h3>
                    ${event.title}
                </h3>

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
                    <strong>Available:</strong>
                    ${availableSeats}
                </p>

                <button
                    class="primary-btn"
                    onclick="editEvent(${event.id})">

                    Edit

                </button>

                <button
                    class="danger-btn"
                    onclick="deleteEvent(${event.id})">

                    Delete

                </button>

                <button
                    class="secondary-btn"
                    onclick="viewEventRegistrations(${event.id})">

                    View Registrations

                </button>

            `;


            container.appendChild(card);

        }


    } catch (error) {

        console.error(error);

        alert(
            "Unable to connect to the server"
        );

    }

}


// =====================================================
// EDIT EVENT
// =====================================================

async function editEvent(eventId) {

    if (!currentOrganizer) {

        return;
    }


    const title =
        prompt(
            "Enter new event title:"
        );


    if (title === null) {

        return;
    }


    const date =
        prompt(
            "Enter new event date (YYYY-MM-DD):"
        );


    if (date === null) {

        return;
    }


    const venue =
        prompt(
            "Enter new venue:"
        );


    if (venue === null) {

        return;
    }


    const capacity =
        prompt(
            "Enter new capacity:"
        );


    if (capacity === null) {

        return;
    }


    const eventData = {

        title: title,

        date: date,

        venue: venue,

        capacity: Number(capacity)

    };


    try {

        const url =
            API_BASE +
            "/events/" +
            eventId +
            "?organizerId=" +
            currentOrganizer.id;


        const response =
            await fetch(url, {

                method: "PUT",

                headers: {

                    "Content-Type":
                        "application/json"

                },

                body:
                    JSON.stringify(eventData)

            });


        const data =
            await response.json();


        if (!response.ok) {

            alert(
                data.error ||
                "Event update failed"
            );

            return;
        }


        alert(
            "Event updated successfully!"
        );


        loadOrganizerEvents();


    } catch (error) {

        console.error(error);

        alert(
            "Unable to connect to the server"
        );

    }

}


// =====================================================
// DELETE EVENT
// =====================================================

async function deleteEvent(eventId) {

    if (!currentOrganizer) {

        return;
    }


    const confirmation =
        confirm(
            "Are you sure you want to delete this event?"
        );


    if (!confirmation) {

        return;
    }


    try {

        const url =
            API_BASE +
            "/events/" +
            eventId +
            "?organizerId=" +
            currentOrganizer.id;


        const response =
            await fetch(url, {

                method: "DELETE"

            });


        const data =
            await response.text();


        if (!response.ok) {

            let errorMessage =
                "Unable to delete event";


            try {

                const errorData =
                    JSON.parse(data);

                errorMessage =
                    errorData.error ||
                    errorMessage;

            } catch (error) {

                // Ignore JSON parsing error

            }


            alert(errorMessage);

            return;
        }


        alert(
            "Event deleted successfully!"
        );


        loadOrganizerEvents();


    } catch (error) {

        console.error(error);

        alert(
            "Unable to connect to the server"
        );

    }

}


// =====================================================
// VIEW EVENT REGISTRATIONS
// =====================================================

async function viewEventRegistrations(
    eventId
) {

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

            alert(
                registrations.error ||
                "Unable to load registrations"
            );

            return;
        }


        const container =
            document.getElementById(
                "eventRegistrationsContainer"
            );


        if (registrations.length === 0) {

            container.innerHTML =
                "<p>No students registered for this event.</p>";

            return;
        }


        let html = `

            <table>

                <tr>

                    <th>
                        Registration ID
                    </th>

                    <th>
                        Student Name
                    </th>

                    <th>
                        Student Email
                    </th>

                </tr>

        `;


        registrations.forEach(
            function(registration) {

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


        html += "</table>";


        container.innerHTML = html;


    } catch (error) {

        console.error(error);

        alert(
            "Unable to connect to the server"
        );

    }

}


// =====================================================
// START PAGE
// =====================================================

showSection("home");