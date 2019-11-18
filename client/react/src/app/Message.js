import LocalizedStrings from "react-localization";
import {APP_DEFAULT_LOCALE, COOKIES_LOCALE} from "../constants";

export let message = {
    en: {
        //navbar
        newCertificate: "Add new certificate",
        name: "Gift Certificates",
        certificates: "Certificates",
        profile: "Profile",
        logout: "Logout",
        login: "Log in",
        signup: "Sign up",

        //name
        slogan: "Gift certificates service. Enjoy the moment",

        //footer
        footer: "Certificates © 2019",


        successfullLogout: "You're safely logged out!",
        notFound: "The Page you're looking for was not found.",
        areYouSure: "Do you want to delete this certificate?",
        ok: "Ok",
        cancel: "Cancel",

        //certificates
        add: "Add",
        delete: "Delete",
        edit: "Edit",
        buy: "Buy",
        certificatesLabel: "Certificates",
        searchParams: "Search...",
        searchViolation: "#(tagname) to search by tag, $(200-400) to search by price, usual text to search by title or description",
        noCertificates: "Nothing found",
        searchCommand: "Go!",
        notReadableSearch: "Could not understand your query...sorry",

        //add/edit
        title: "Title",
        description: "Description",
        price: "Price",
        expiration: "Expiration date",
        addNewTag: "add new tag",
        addEditLabel: "Add/Edit Certificate",
        titleViolation: "certificate name should contain letters, digits, spaces or -.",
        descriptionViolation: "description should be 1-1000 symbols.",
        priceViolation: "Price must be a number or float and be greater than 0",
        expirationViolation: "Invalid date",
        expiring: "Expiring in: ",
        tags: "Tags: ",
        certificateUpdated: "certificate successfully updated!",
        certificateAdded: "certificate successfully added!",
        certificateDeleted: "certificate successfully deleted!",
        error: "Oops! Something went wrong. Please try again!",
        unsavedData: "You have unsaved data. Do you want to leave this page?",

        //login
        loginLabel: "Login to Gift Certificates",
        or: "OR",
        newUser: "New user? ",
        signUpLink: "Sign up!",
        loginGoogle: " Log in with Google",
        loginFacebook: " Log in with Facebook",
        loginSuccess: "You're successfully logged in! ",
        emailPlaceholder: "Enter your email",
        passwordPlaceholder: "Enter password",

        //sign up
        signupLabel: "Sign up to Gift Certificates",
        haveAccount: "Already have an account? ",
        loginLink: "Login!",
        signUpGoogle: " Sign up with Google",
        signUpFacebook: " Sign up with Facebook",
        signUpSuccess: "You're successfully registered!",
        passwordViolation: "Password must be minimum 8, maximum 20 symbols, and contain at least 1 number, 1 latin uppercase letter, 1 latin lowercase letter, 1 punctuation. Only latin letters available, spaces are unavailable",
        emailViolation: "invalid email",

        //certificate modal
        descriptionModal: "Description ",
        priceModal: "Price ",
        expiresModal: "Expires: ",
        createdModal: "Created: ",

        //buy button modals
        notAuthorized: "You are not authorized, please log in first",
        later: "Later",
        confirmPurchase: "Please confirm your purchase",

        //profile
        admin: "Admin",

        //basket
        thanks: "Thanks for your order! ",
        cart: "Cart",
        emptyCart: "Empty cart",
        pay: "pay total price: ",
        del: "remove",
        addToCart: "Add certificate to cart? ",
        toCart: "Confirm",

        //pagination
        firstPage: "first",
        lastPage: "last",
    },
    ru: {
        //navbar
        newCertificate: "Создать сертификат",
        name: "Gift Certificates",
        certificates: "Сертификаты",
        profile: "Профайл",
        logout: "Выйти",
        login: "Войти",
        signup: "Регистрация",

        //name
        slogan: "Сервис продажи сертификатов. Насладись моментом",

        //footer
        footer: "Сертификаты © 2019",


        successfullLogout: "Вы успешно вышли из приложения!",
        notFound: "Страница не найдена.",
        areYouSure: "Вы действительно хотите удалить этот сертификат?",
        ok: "Ок",
        cancel: "Отмена",

        //certificates
        add: "Добавить",
        delete: "Удалить",
        edit: "Редактировать",
        buy: "Купить",
        certificatesLabel: "Сертификаты",
        searchParams: "Поиск...",
        searchViolation: "#(tagname) для поиска по имени тэга, $(200-400) для поиска по цене, обычный текст для поиска по имени или описанию",
        noCertificates: "Ничего не найдено",
        searchCommand: "Искать!",
        notReadableSearch: "Не могу понять ваш запрос...извините",

        //add/edit
        title: "Название",
        description: "Описание",
        price: "Цена",
        expiration: "Дата окончания",
        addNewTag: "добавить новый тэг",
        addEditLabel: "Добавить/Отредактировать Сертификат",
        titleViolation: "имя сертификата должно содержать буквы, цифры, пробелы и -",
        descriptionViolation: "описание должно быть 1-1000 символов",
        priceViolation: "Цена должна быть целой или дробной цифрой и быть больше 0",
        expirationViolation: "Некорректная дата",
        expiring: "Истекает: ",
        tags: "Тэги: ",
        certificateUpdated: "сертификат отредактирован!",
        certificateAdded: "сертификат добавлен!",
        certificateDeleted: "сертификат удален!",
        error: "Что-то пошло не так...",
        unsavedData: "Имеются несохраненные данные. Вы уверены что хотите покинуть страницу?",


        //login
        loginLabel: "Войти в Gift Certificates",
        or: "ИЛИ",
        newUser: "Новый пользователь? ",
        signUpLink: "Зарегистрироваться!",
        loginGoogle: " Войти с Google",
        loginFacebook: " Войти с Facebook",
        loginSuccess: "Вы успешно вошли в приложение! ",
        emailPlaceholder: "Ведите ваш email",
        passwordPlaceholder: "Введите пароль",

        //sign up
        signupLabel: "Зарегистрироваться в Gift Certificates",
        haveAccount: "Уже есть аккаунт? ",
        loginLink: "Войти!",
        signUpGoogle: " Зарегистрироваться с Google",
        signUpFacebook: " Зарегистрироваться с Facebook",
        signUpSuccess: "Вы успешно зарегестрировались!",
        passwordViolation: "Пароль должен содержать не менее 8, не более 20 символов и содержать как минимум 1 цифру, 1 латинскую заглавную букву, 1 латинскую строчную букву, 1 пунктуацию. Доступны только латинские буквы, пробелы недоступны",
        emailViolation: "не валидный email",

        //certificate modal
        descriptionModal: "Описание ",
        priceModal: "Цена ",
        expiresModal: "Истекает: ",
        createdModal: "Создан: ",

        //buy button modal
        notAuthorized: "Вы не авторизованы, пройдите на страницу входа",
        later: "Позже",
        confirmPurchase: "Подтвердите вашу покупку",

        //profile
        admin: "Администратор",

        //basket
        thanks: "Спасибо за ваш заказ! ",
        cart: "Корзина",
        emptyCart: "Ваша корзина пуста",
        pay: "оплатить заказ на сумму: ",
        del: "удалить",
        addToCart: "Добавить сертификат в корзину? ",
        toCart: "Добавить",

        //pagination
        firstPage: "первая",
        lastPage: "последняя",
    }
};

export function getMessage(props, key) {
    const {cookies} = props;
    let strings = new LocalizedStrings({message});
    strings.setContent(message);
    strings.setLanguage(cookies.get(COOKIES_LOCALE) ? cookies.get(COOKIES_LOCALE) : APP_DEFAULT_LOCALE);
    return strings.getString(key)
}

export function getMessageByLocale(locale, key) {
    let strings = new LocalizedStrings({message});
    strings.setContent(message);
    strings.setLanguage(locale ? locale : APP_DEFAULT_LOCALE);
    return strings.getString(key)
}
