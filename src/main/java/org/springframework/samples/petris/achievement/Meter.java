package org.springframework.samples.petris.achievement;

public enum Meter {
    MATCH("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTd-8aDTzrK3ek8sN1rfBiNqlGOD2rWgOfhYcOvYd-baVwHC0cQqmSJjoTsxI-AJVO23hY&usqp=CAU"), 
    VICTORY("https://previews.123rf.com/images/tatianasun/tatianasun1703/tatianasun170300104/75047415-pulgar-arriba-vector-logo-grunge-icono-verde-signo-como-s%C3%ADmbolo-redondo-simple-aislado.jpg"), 
    DEFEAT("https://st2.depositphotos.com/5266903/8942/v/950/depositphotos_89427162-stock-illustration-thumb-down-rounded-vector-icon.jpg");

    private String uri;

    Meter(String uri){
        this.uri = uri;
    }

    public String getUri(){
        return this.uri;
    }
}
