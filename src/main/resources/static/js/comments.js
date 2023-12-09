const id = document.getElementById('articleId').value

const csrfHeaderName = document.head.querySelector('[name="_csrf_header"]').content;
const csrfHeaderValue = document.head.querySelector('[name="_csrf"]').content;

const commentsList = document.getElementById('comment-list')

const commentForm = document.getElementById('commentForm')
commentForm.addEventListener("submit", handleCommentSubmit)

const allComments = []

const displayComments = (comments) => {
    commentsList.innerHTML = comments.map(
        (c)=> {
            return asComment(c)
        }
    ).join('')
}

async function handleCommentSubmit(event) {
    event.preventDefault();

    const form = event.currentTarget;
    const url = form.action;
    const formData = new FormData(form);

    try {
        const responseData = await postFormDataAsJson({url, formData});

        commentsList.insertAdjacentHTML("afterbegin", asComment(responseData));

        form.reset();
    } catch (error) {

        let errorObj = JSON.parse(error.message);

        if (errorObj.fieldWithErrors) {
            errorObj.fieldWithErrors.forEach(
                e => {
                    let elementWithError = document.getElementById(e);
                    if (elementWithError) {
                        elementWithError.classList.add("is-invalid");
                    }
                }
            )
        }
    }
    console.log('going to submit a comment!')
}

async function postFormDataAsJson({url, formData}) {

    const plainFormData = Object.fromEntries(formData.entries());
    const formDataAsJSONString = JSON.stringify(plainFormData);

    const fetchOptions = {
        method: "POST",
        headers: {
            [csrfHeaderName] : csrfHeaderValue,
            "Content-Type" : "application/json",
            "Accept" :"application/json"
        },
        body: formDataAsJSONString
    }

    const response = await fetch(url, fetchOptions);

    if (!response.ok) {
        const errorMessage = await response.text();
        throw new Error(errorMessage);
    }

    return response.json();
}
function asComment(c) {
    let commentHtml = `<li class="comment bg-light">`

    commentHtml += `<div class="vcard bio"><img src="/images/anonymous-profile-pic.jpg" alt="Image placeholder"></div>`
    commentHtml += `<div class="comment-body">
        <h3>${c.authorName}</h3>
        <div class="meta">${c.published}</div>
        <p class="bg-white">${c.text}</p>
                    </div>`

    return commentHtml
}

fetch(`http://localhost:8080/blog/article/${id}/comments`).
then(response => response.json()).
then(data => {
    for (let comment of data) {
        allComments.push(comment)
    }
    displayComments(allComments)
})
