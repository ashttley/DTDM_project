<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Document</title>
</head>
<body>

    <form action="{{url('d/'.$key)}}" method="post">
        @csrf
        @method('PUT')
        USername<input type="text" name="username" value="{{$editdata['username']}}">
Password <input type="text" name="password" value="{{$editdata['password']}}">
PHone <input type="text" name="phone" value="{{$editdata['phone']}}">
email <input type="text" name="email" value="{{$editdata['email']}}">
<button type="submit">Save</button>
    </form>
</body>
</html>
