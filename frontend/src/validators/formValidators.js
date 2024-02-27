export const formValidators = {
    notEmptyValidator: {
        validate: (value) => {
            return value.trim().length > 0;
        },
        message: "The field cannot be empty"
    },
    telephoneValidator: {
        validate: (value) => {
            return value.trim().length === 9 && /^\d+$/.test(value);
        },
        message: "The telephone number must be 9 digits long and contain only numbers"
    },
    passwordValidator:{
        validate: (value) => {
            return value.trim().length >= 8 && /^(?=.*[a-zA-Z])(?=.*\d).+$/.test(value);
        },
        message: "La contraseña debe de tener mínimo 8 caracteres y tener letras y números"
    },
    emailValidator: {
        validate: (value) => {
            return /^[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,3}$/i.test(value);
        },
        message: "Formato de email incorrecto"
    },
    notNoneTypeValidator: {
        validate: (value) => {
            return value !== "None";
        },
        message: "Please, select a type"
    },
    validPhoneNumberValidator: {
        validate: (value) => {
            return value.trim().length === 9 && /^\d+$/.test(value);
        },
        message: "The phone number must be 9 digits long and contain only numbers"
    }
}