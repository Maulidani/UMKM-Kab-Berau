<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use App\Models\Umkm;

use function PHPUnit\Framework\isEmpty;
use function PHPUnit\Framework\isNull;

class UmkmController extends Controller
{
    public function index(Request $request)
    {
        $jenisUmkm = $request->jenis_umkm;
        $jenisProduk = $request->jenis_produk;
        $product = Umkm::where([
            ['jenis_umkm', '=', $jenisUmkm],
            ['jenis_produk', '=', $jenisProduk]
        ])
            ->get();

        $productDataNorm = Umkm::where([
            ['jenis_umkm', '=', $jenisUmkm],
            ['jenis_produk', '=', $jenisProduk]
        ])
            ->get(['harga','rating']);

        if ($product->isEmpty()) {
            return response()->json([
                'message' => 'Failed',
                'errors' => true,
            ]);
        } else {
            return response()->json([
                'message' => 'Success',
                'errors' => false,
                'data' => $product,
            ]);
        }
    }
}
