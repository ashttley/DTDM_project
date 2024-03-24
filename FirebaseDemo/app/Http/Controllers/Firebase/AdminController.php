<?php

namespace App\Http\Controllers\Firebase;

use App\Http\Controllers\Controller;
use Illuminate\Http\Request;
use Kreait\Firebase\Contract\Database;
use Kreait\Firebase\Factory;
use Kreait\Firebase\Storage;

class AdminController extends Controller
{
    //
    public function __construct(Database $database)
    {
        $this->database = $database;
        $this->tablename = 'admins';
    }
    public function index(){
        $admins = $this->database->getReference($this->tablename)->getValue();
        return view('index',compact('admins'));
    }
    public function create(){
        return view('create');
    }
    public function store(Request $request){

        $postData = [
            'username' => $request->username,
            'password' => $request->password,
            'phone' => $request->phone,
            'email' => $request->email
        ];
        $postRef = $this->database->getReference($this->tablename)->push($postData);
        if($postRef){
            return redirect('contacts')->with('status','Contact added successfully');
        }else{
            return redirect('contacts')->with('status','Contact not added');
        }
    }
    public function edit($id){
        $key = $id;
        $editdata = $this->database->getReference($this->tablename)->getChild($key)->getValue();
        if($editdata){
            return view('edit',compact('editdata','key'));
        }else{
            return redirect('b')->with('status','Admin not found');
        }

    }
    public function update(Request $request, $id){
        $key = $id;
        $updateData = [
            'username' => $request->username,
            'password' => $request->password,
            'phone' => $request->phone,
            'email' => $request->email
        ];
        $this->database->getReference($this->tablename.'/'.$key)->update($updateData);
    }
    public function destroy($id){
        $key = $id;
        $this->database->getReference($this->tablename.'/'.$key)->remove();
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

        // Here you can save $imageUrl to your database or return it in the response
        return response()->json(['success' => true, 'url' => $imageUrl]);
    }
}
