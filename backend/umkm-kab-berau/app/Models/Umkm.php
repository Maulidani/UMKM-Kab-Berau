<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\Factories\HasFactory;

class Umkm extends Model
{
    use HasFactory;
    public $timestamps = false;

    protected $table = 'umkms';

    protected $fillable = [
       'id', 'nama', 'nama_produk', 'jenis_umkm','jenis_produk','harga','rating','telepon','deskripsi','img','latitude','longitude',
    ];

}