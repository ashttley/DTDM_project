<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use Kreait\Firebase\Contract\Database;
use Kreait\Firebase\Factory;
use Kreait\Firebase\Storage;

class ProductController extends Controller
{
    //
    public function __construct(Database $database){
        $this->database = $database;
        $this->tablename = 'products';
    }
    public function uploadImage(Request $request)
    {
        $request->validate([
            'image' => 'required|image|max:1024', // 1MB Max
        ]);

        $image = $request->file('image');

        $imageName = time().'_'.$image->getClientOriginalName();

        $firebaseStorage = (new Factory)
            ->withServiceAccount(base_path(env('FIREBASE_CREDENTIALS')))
            ->withDatabaseUri(env('FIREBASE_DATABASE_URL'))
            ->createStorage();

        $storageBucket = $firebaseStorage->getBucket();

        $storagePath = 'images/' . $imageName;

        $storageBucket->upload(file_get_contents($image), [
            'name' => $storagePath
        ]);

        // Assuming you want to save the URL in the database or return it in the response
        $imageUrl = sprintf('https://firebasestorage.googleapis.com/v0/b/%s/o/%s?alt=media', env('FIREBASE_STORAGE_BUCKET'), urlencode($storagePath));

        $postData = [
            'name' => $request->name,
            'description' => $request->description,
            'imageUrl' => $imageUrl,
            'status' => "Full"
        ];
        $this->database->getReference($this->tablename)->push($postData);
        // Here you can save $imageUrl to your database or return it in the response
        return response()->json(['success' => true, 'url' => $imageUrl]);
    }
}
