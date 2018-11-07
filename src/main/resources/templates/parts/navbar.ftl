<#include "security.ftl">
<#import "login.ftl" as l>


<nav class="navbar navbar-expand-lg navbar-dark bg-primary">
    <a class="navbar-brand" href="/">KonovalovProg</a>
    <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
    </button>

    <div class="collapse navbar-collapse" id="navbarSupportedContent">
        <ul class="navbar-nav mr-auto">
            <li class="nav-item">
                <a class="nav-link" href="/">Home</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="/main">Messages</a>
            </li>
            <#if isAdmin>
            <li class="nav-item">
                <a class="nav-link" href="/user">List users</a>
            </li>
            </#if>
            <#if isAdmin>
            <li class="nav-item">
                <a class="nav-link" href="/messagesList">List messages</a>
            </li>
            </#if>
            <#if user??>
            <li class="nav-item">
                <a class="nav-link" href="/user/profile">Profile</a>
            </li>
            </#if>
        </ul>

        <#--<div class="navbar- mr-3">${name}</div>-->
    <#--<#if singUser??>-->
        <#--<@l.logout />-->
    <#--<#else >-->
        <#--<@l.loginn />-->
    <#--</#if>-->
    <#--</div>-->
        <div class="navbar-text mr-3"><#if user??>${name}, <@l.logout /><#else>Please, login
        <form action="/main" method="Get">
            <button class="btn btn-primary" type="submit">Sing in</button>
        </form></#if></div>

    </div>
</nav>