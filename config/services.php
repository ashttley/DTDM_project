<?php

return [

    /*
    |--------------------------------------------------------------------------
    | Third Party Services
    |--------------------------------------------------------------------------
    |
    | This file is for storing the credentials for third party services such
    | as Mailgun, Postmark, AWS and more. This file provides the de facto
    | location for this type of information, allowing packages to have
    | a conventional file to locate the various service credentials.
    |
    */

    'mailgun' => [
        'domain' => env('MAILGUN_DOMAIN'),
        'secret' => env('MAILGUN_SECRET'),
        'endpoint' => env('MAILGUN_ENDPOINT', 'api.mailgun.net'),
    ],

    'postmark' => [
        'token' => env('POSTMARK_TOKEN'),
    ],

    'ses' => [
        'key' => env('AWS_ACCESS_KEY_ID'),
        'secret' => env('AWS_SECRET_ACCESS_KEY'),
        'region' => env('AWS_DEFAULT_REGION', 'us-east-1'),
    ],

    'firebase' => [
        'api_key' => 'AIzaSyD-VItv68vUL_BA-UYsRF_WnqTMEB3hz_s', // Only used from JS integration
        'auth_domain' => 'laravel-crud-with-fireba-42b7a.firebaseapp.com', // Only used from JS integration
        'database_url' => 'https://laravel-crud-with-fireba-42b7a-default-rtdb.firebaseio.com',
        'storage_bucket' => 'laravel-crud-with-fireba-42b7a.appspot.com', // Only used from JS integration
    ],

];
