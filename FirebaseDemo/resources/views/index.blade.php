<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Document</title>
</head>
<body>
    <img src="gs://foodapp-be64c.appspot.com/waifu.jpg" alt="">
<table>
    @php $i = 1 @endphp
    @forelse($admins as $key => $item)
    <tr>
        <td>{{$i++}}</td>
        <td> {{$item['username']}}</td>
        <td><a href="{{url('c/'.$key)}}">Update</a></td>
        <!-- <td><a href="{{url('e/'.$key)}}">Delete</a></td> -->
        <td><form action="{{url('e/'.$key)}}" method="post">
            @csrf
            @method('DELETE')
            <button type="submit">Delete</button>
        </form></td>
    </tr>
    @empty
    <td>No record Found</td>
    @endforelse
</table>
</body>
</html>
