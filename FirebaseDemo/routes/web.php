<?php

use App\Http\Controllers\Firebase\AdminController;
use App\Http\Controllers\ProductController;
use Illuminate\Support\Facades\Route;

/*
|--------------------------------------------------------------------------
| Web Routes
|--------------------------------------------------------------------------
|
| Here is where you can register web routes for your application. These
| routes are loaded by the RouteServiceProvider and all of them will
| be assigned to the "web" middleware group. Make something great!
|
*/

Route::get('/', function () {
    return view('welcome');
});
Route::get('a',[AdminController::class,'create']);
Route::post('a',[AdminController::class,'store']);
Route::get('b',[AdminController::class,'index']);
Route::get('c/{id}',[AdminController::class,'edit']);
Route::put('d/{id}',[AdminController::class,'update']);
Route::delete('e/{id}',[AdminController::class,'destroy']);
Route::view('/upload','upload');
Route::view('/addProduct','createProduct');
Route::post('/success',[ProductController::class,'uploadImage']);
Route::post('/upload-image', [AdminController::class, 'uploadImage']);

