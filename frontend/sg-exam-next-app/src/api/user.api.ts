import api from './api'

class userApi {

    getSysDefaultConfig() {
        return api.fetchUser<Sg.UserInfo>("/v1/config/getDefaultSysConfig", {}, "GET");
    }

    userInfo() {
        return api.fetchUser<Sg.UserInfo>("/v1/user/info?identityType=4", {}, "GET");
    }

    checkExist(tenantCode: string, username: string) {
        return api.fetchUserTenant<Sg.UserInfo>("/v1/user/anonymousUser/checkExist/" + username, {identityType: 1}, "GET", tenantCode);
    }

    register(tenantCode: string, data: object) {
        Object.assign(data, {identityType: 1});
        return api.fetchUserTenant<Sg.UserInfo>("/v1/user/anonymousUser/register?ignoreCode=1", data, "POST", tenantCode);
    }

    sendSms(phone: string) {
        const tenantCode = api.getTenantCode();
        return api.fetchUserTenant<any>("/v1/mobile/sendSms/" + phone, {}, "GET", tenantCode);
    }

    sendSmsTenant(phone: string, tenantCode: string | undefined) {
        return api.fetchUserTenant<any>("/v1/mobile/sendSms/" + phone, {}, "GET", tenantCode);
    }

    bindPhoneNumber(userInfo: object) {
        return api.fetchUser<Sg.UserInfo>("/v1/user/bindPhoneNumber", userInfo, "PUT");
    }

    updateInfo(userInfo: object) {
        return api.fetchUser<Sg.UserInfo>("/v1/user/updateInfo", userInfo, "PUT");
    }

    uploadAudio(filePath: string, data: Object = {}) {
        return api.uploadUser<any>("/v1/speechSynthesis/uploadSpeech", filePath, data)
    }

    getNotice() {
        return api.fetchUser<any>("/v1/notice/getNotice", {}, "GET");
    }

    getMessages(userId: string, type: number) {
        return api.fetchUser<any>("/v1/message/userMessageList", {receiverId: userId, type: type}, "GET");
    }

    getMessageDetail(id: string) {
        return api.fetchUser<any>("/v1/message/" + id, {}, "GET");
    }
}

export default new userApi()
