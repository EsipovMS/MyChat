let newMessageArea = document.querySelector('#new_message');
let typingStatusField = document.querySelector('#typing_status')
let token = $("meta[name='_csrf']").attr("content");
let isTyping = false;

setInterval(getDstPersonTyping, 1000);

newMessageArea.addEventListener('keyup', function (event) {
    if (newMessageArea.value === "" && isTyping === true) {
        isTyping = false;
        setTypingStatus(isTyping);
    }
    if (newMessageArea.value !== "" && isTyping === false) {
        isTyping = true;
        setTypingStatus(isTyping);
    }
})

function setTypingStatus(isTyping) {
    $.ajax({
        type: 'POST',
        url: '/typing',
        data: {isTyping : JSON.parse(isTyping), _csrf : token}
    });
}



function getDstPersonTyping() {
    $.ajax({
        type: 'GET',
        url: '/typing',
        success: function (result){
            if (result === true)
                typingStatusField.innerHTML = "печатает..."
            if (result === false) {
                typingStatusField.innerHTML = "";
            }
        }
    });
}