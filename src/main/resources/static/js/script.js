let messagesList = $('.message-holder');
const mediaQuery = window.matchMedia('(max-width: 768px)');
let resScroll;
let scrollHeight;
let block;
let tempMessages = [];
let answeredMessageId;
document.onload = setTimeout(setScroll, 1000);
setupBurgerButton();
becomeMessages();
setMessageBlockHeight();
getChatDetails();
setSendButton();

setInterval(getChatDetails, 1000);

function getScroll() {
    block = document.querySelector('#messages_block');
    if (block === null) return;
    $('#messages_block').scroll(function () {
        scrollHeight = block.scrollHeight;
        resScroll = parseInt(block.style.height.replace('px', '')) + block.scrollTop;
    })
}

function setScroll() {
    block = document.querySelector('#messages_block');
    if (block === null) return;
    block.scroll(0, Number.MAX_SAFE_INTEGER);
}

function setSendButton() {
    let sendButton = $('#send_button');
    sendButton.click('click', function () {
        let messageField = document.querySelector('#new_message');
        let token = $("meta[name='_csrf']").attr("content");

        let formData = new FormData();
        formData.append('textMessage', messageField.value);
        formData.append('_csrf', token);
        formData.append('image', $("#input__file")[0].files[0]);
        formData.append('answeredMessageId', answeredMessageId);

        $.ajax({
            type: 'POST',
            url: '/messages',
            contentType: false,
            processData: false,
            data: formData
        })
        $("#input__file").val(null);
        isTyping = false;
        setTypingStatus(isTyping);
        getMessages(true);
        messageField.value = "";
        answeredMessageId = null;
    })
}

function setControls() {
    $(document).off("click", ".message-controls");
    $(document).on("click", ".message-controls", function () {

        let messageId = this.parentNode.parentNode.id;
        let messageControlsDisplay = this.parentNode.querySelector(".message-controls-display");
        if (messageControlsDisplay.style.display === 'block') {
            messageControlsDisplay.style.display = 'none';
            return;
        }
        messageControlsDisplay.style.display = 'block';
        let deleteButton = messageControlsDisplay.querySelector(".deleteButton");
        let answerButton = messageControlsDisplay.querySelector(".answerButton");

        deleteButton.addEventListener("click", function (event) {
            if (confirm("Действительно хотите удалить сообщение?")) {
                deleteMessage(messageId);
                setTimeout(() => getMessages(false), 100);
            }
        });

        answerButton.addEventListener("click", function (event) {
            answeredMessageId = messageId;
            messageControlsDisplay.style.display = 'none';
        });
    })
}

function deleteMessage(messageId) {
    var token = $("meta[name='_csrf']").attr("content");
    $.ajax({
        type: 'DELETE',
        url: "/messages/" + messageId,
        data: {_csrf : token}
    })
}

window.addEventListener('resize', function(event){
    calcMessageBoxSize();
})

function switchDisplay(el) {
    let style = window.getComputedStyle(el);
    let display = style.getPropertyValue('display');
    if (display === 'none') {
        el.style.display = 'block';
    } else {
        el.style.display = 'none';
    }
}

var tx = document.getElementsByTagName('textarea');

for (var i = 0; i < tx.length; i++) {

tx[i].setAttribute('style', 'height:' + (tx[i].scrollHeight) + 'px;overflow-y:hidden;');

tx[i].addEventListener("input", OnInput, false);

}

function OnInput() {

this.style.height = 'auto';

this.style.height = (this.scrollHeight) + 'px';

}

function setupBurgerButton() {
    const burgerButton = document.querySelector('.chatbox__header-burger-button');
    const contactsBlock = document.querySelector('.chatbox__contacts-block');
    if (burgerButton == null) return;
    burgerButton.addEventListener('click', function(event) {
        switchDisplay(contactsBlock);
    })
}

if(mediaQuery.matches) {
    calcMessageBoxSize();
}

function calcMessageBoxSize() {
    const messageBox = document.querySelector('.chatbox__messages-block');
    if (messageBox == null) return;
    const chatboxHeader = document.querySelector('.chatbox__header');
    const chatboxInput = document.querySelector('.chatbox__input-space');
    let screenHeight = window.innerHeight;
    let messageBoxSize = screenHeight - chatboxInput.scrollHeight - chatboxHeader.scrollHeight + 3;
    messageBox.style.height = messageBoxSize + 'px';
}

function getChatDetails() {
    const dstPersonName = document.querySelector('.dst_person');
    if (dstPersonName == null) return;
    var token = $("meta[name='_csrf']").attr("content");
    $.ajax({
        type: 'GET',
        url: '/chatDetails',
        data: {_csrf : token},
        success: function (result) {
            $('.dst_person').html(result.dstPerson.name + " " + result.dstPerson.firstName);
            let onlineStatus = "";
            if (result.dstPerson.onlineStatus === true) {
                onlineStatus = "online";
            }
            $('#online_status').html(onlineStatus);
        }
    });
}

function getMessages(scrollDown) {
    const messagesBlock = document.querySelector('#messages_block');
    if (messagesBlock == null) return;
    var token = $("meta[name='_csrf']").attr("content");
    $.ajax({
        type: 'GET',
        url: '/messages',
        data: {_csrf : token},
        success: function (result) {
            if (JSON.stringify(tempMessages) === JSON.stringify(result)) return;
            tempMessages = result;
            $('#messages_block').html(getMessagesHtml(result));
            if (scrollDown) setScroll();
            getScroll();
            if (scrollHeight - resScroll >= 20) return;
            setScroll();
        }
    });
    setControls();
}

function getMessagesHtml(result) {

    let messagesHtml = "";

    for (let messageRsItem of result) {
        let income = "";
        if (messageRsItem.income) {
            income = "income"
        }

        let readStatus = "";

        if (messageRsItem.status !== "READ") {
            readStatus = "unread ";
        }

        let statusString = "";
        if (messageRsItem.income) {
            statusString = "<div class=\"message-status\">" + `${messageRsItem.status}` + "</div>\n"
        }
        let imagePart = ""
        if (messageRsItem.imageId !== null) {
            let imageId = parseInt(messageRsItem.imageId);
            imagePart = "            <div class='image-holder'><img class='message-image' src='/images/" + imageId +"'></div>"
        }

        let answeredMessagePart = "";
        if (messageRsItem.answeredMessage !== null) {
            let ansMessageText = messageRsItem.answeredMessage.messageText;
            let incomeAnsMessage = "";
            if (!messageRsItem.income) {
                incomeAnsMessage = " incomeAnsMessage";
            }
            answeredMessagePart = "<div class='answeredMessageHolder" + incomeAnsMessage + "'>" +
                "<p class='ansMessageLabel'>Ответ на сообщение:</p>" +
                "<p class='answeredMessageText'>" + ansMessageText + "</p></div>"
        }

        messagesHtml +=

            "    <div id=\"" + `${messageRsItem.id}` + "\" class=\"message-holder\">\n" +
            answeredMessagePart +
            "        <div class=\"message-body " + `${readStatus}` + `${income}` + "\">\n" +
            "            <div class=\"message-controls\">&rsaquo;</div>\n" +
            "            <div class=\"message-controls-display\">\n" +
            "                <a href=\"#\">\n" +
            "                    <p class=\"message-control-item deleteButton\">Удалить</p>\n" +
            "                </a>\n" +
            "                <a href=\"#\">\n" +
            "                    <p class=\"message-control-item answerButton\">Ответить</p>\n" +
            "                </a>\n" +
            "            </div>\n" +
            imagePart +
            "            <div class=\"message-text\">" + `${messageRsItem.messageText}` + "</div>\n" +
            "            <div class=\"message-date-time\">" + `${messageRsItem.time}` + "</div>\n" +
            statusString             +
            "        </div>\n" +
            "    </div>"
    }

    return messagesHtml;
}

function becomeMessages() {
    getMessages(true);
    setInterval(() => getMessages(false), 1500);
}

function setMessageBlockHeight() {
    if (mediaQuery.matches) return;
    const messagesBlock = document.querySelector('#messages_block');
    if(messagesBlock === null) return;
    let height = window.screen.height * 0.7;
    messagesBlock.style.height = height.toString() + "px";
}


