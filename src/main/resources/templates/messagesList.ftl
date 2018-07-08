<#import "parts/common.ftl" as c>

<@c.page>
Messages List
<form action="/messagesList" method="post">
    <#list messages as message>
        <div>
            <input type="checkbox" name="toDelete[]" value="${message.id}" id="checkbox_${message.id}" >id: ${message.id} message: ${message.text}</>
        </div>
    </#list>
    <input type="hidden" value="${_csrf.token}" name="_csrf">
    <button type="submit">Delete</button>
</form>
</@c.page>[