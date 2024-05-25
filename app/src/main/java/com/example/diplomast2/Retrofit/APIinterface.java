package com.example.diplomast2.Retrofit;

import android.widget.Space;

import com.example.diplomast2.DTO.Admin;
import com.example.diplomast2.DTO.Note;
import com.example.diplomast2.DTO.Point;
import com.example.diplomast2.DTO.Point;
import com.example.diplomast2.DTO.Specialist;
import com.example.diplomast2.DTO.Timeline;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface APIinterface {
    @Headers("Accept: application/json") //Авторизация администратора
    @GET("Admin/{login}&{password}")
    Call<Admin> getAdminAccount(@Path("login") String login, @Path("password") String password);
    @Headers("Accept: application/json") //Получение списка администраторов
    @GET("Admins")
    Call<List<Admin>> getAllAdmins();
    @Headers("Accept: application/json") //Добавление нового администратора
    @POST("Admin/Insert")
    Call<Void> insertAdmin(@Body Admin admin);
    @Headers("Accept: application/json") //Удаление администратора
    @DELETE("Admin/{id}/Delete")
    Call<Void> deleteAdmin(@Path("id") int id);
    @Headers("Accept: application/json") //Получение списка заявок специалистов
    @GET("Specialists/3")
    Call<List<Specialist>> getSpecialistsApplications();
    @Headers("Accept: application/json") //Одобрение заявки специалиста
    @POST("Specialist/3/{id}/Approve")
    Call<Void> approveSpecialistProfile(@Path("id") int id);
    @Headers("Accept: application/json") //Отклонение заявки специалиста
    @POST("Specialist/3/{id}/Reject")
    Call<Void> rejectSpecialistProfile(@Path("id") int id);
    @Headers("Accept: application/json") //Получение списка специалистов
    @GET("Specialists/1")
    Call<List<Specialist>> getSpecialists1();
    @Headers("Accept: application/json") //Получение конкретного специалиста
    @GET("Specialist/{id}")
    Call<Specialist> getSpecialistById(@Path("id") int id);
    @Headers("Accept: application/json") //Блокировка специалиста
    @POST("Specialist/{id}/Block")
    Call<Void> blockSpecialist(@Path("id") int id);
    @Headers("Accept: application/json") //Получение списка заметок на рассмотрении
    @GET("Notes/3")
    Call<List<Note>> getNotes3();
    @Headers("Accept: application/json") //Одобрение заметки
    @POST("Note/{id}/Approve")
    Call<Void> approveNote(@Path("id") int id);
    @Headers("Accept: application/json") //Отклонение заметки
    @POST("Note/{id}/Reject")
    Call<Void> rejectNote(@Path("id") int id);
    @Headers("Accept: application/json")
    @GET("Specialist/{id}/Points")
    Call<List<Point>> getSpecialistPoints(@Path("id") int id);
    @Headers("Accept: application/json")
    @GET("Timelines")
    Call<List<Timeline>> getAllTimelines();
}
